package as.leap.code.impl;

import as.leap.code.*;
import as.leap.code.LASException;
import as.leap.las.sdk.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class LASClassManagerImpl<T> implements LASClassManager<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LASClassManagerImpl.class);
  private LASClassManagerHook<T> hook;
  private Class<T> entityClazz;
  private String apiAddress;

  public LASClassManagerImpl(LASClassManagerHook<T> hook, Class<T> entityClazz) {
    this.hook = hook;
    this.entityClazz = entityClazz;
    this.apiAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + "/classes/" + entityClazz.getSimpleName();
  }

  @Override
  public SaveResult<T> create(T object, UserPrincipal userPrincipal) throws LASException {
    try {
      BeforeResult<T> beforeResult = hook == null ? new BeforeResult<T>(object, true) : hook.beforeCreate(object, userPrincipal);
      if (!beforeResult.isResult()) return new SaveResult<T>(beforeResult.getFailMessage());
      String response = WebUtils.doPost(apiAddress, CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(object), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of create[" + apiAddress + "]:" + response);
      SaveMsg saveMsg = LASJsonParser.asObject(response, SaveMsg.class);
      SaveResult saveResult = new SaveResult<T>(beforeResult, saveMsg);
      if (hook == null) return saveResult;
      AfterResult afterResult = hook.afterCreate(beforeResult, saveMsg, userPrincipal);
      if (!afterResult.isSuccess()) saveResult.setFailMessage(afterResult.getFailMessage());
      return saveResult;
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public FindMsg<T> find(LASQuery query, UserPrincipal userPrincipal) {
    return find(query, false, userPrincipal);
  }

  @Override
  public FindMsg<T> find(LASQuery query, boolean count, UserPrincipal userPrincipal) throws LASException {
    try {
      String postQuery = serializeLasQueryForPostQuest(query);
      String response = WebUtils.doPost(apiAddress + "/query", CloudCodeContants.getHeaders(userPrincipal), postQuery, CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of find[" + apiAddress + "/query](" + postQuery + "):" + response);
      JsonNode responseJson = LASJsonParser.asJsonNode(response);
      ArrayNode results = (ArrayNode) responseJson.get("results");
      List<T> r = new ArrayList<T>();
      if (results == null || results.size() == 0) return new FindMsg<T>();
      for (JsonNode result : results) {
        r.add(LASJsonParser.asObject(result.toString(), entityClazz));
      }
      return new FindMsg<T>(count ? results.size() : 0, r);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public T findById(String id, UserPrincipal userPrincipal) throws LASException {
    try {
      String response = WebUtils.doGet(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), null);
      LOGGER.info("get response of findById[" + apiAddress + "/" + id + "]:" + response);
      if ("{}".equals(response)) return null;
      return LASJsonParser.asObject(response, entityClazz);
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  @Override
  public UpdateMsg update(String id, LASUpdate update, UserPrincipal userPrincipal) throws LASException {
    try {
      String response = WebUtils.doPut(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(update.update()), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of update[" + apiAddress + "/" + id + "](" + update.update() + "):" + response);
      UpdateMsg updateMsg = LASJsonParser.asObject(response, UpdateMsg.class);
      if (hook != null) hook.afterUpdate(id, updateMsg, userPrincipal);
      return updateMsg;
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  @Override
  public DeleteResult delete(String id, UserPrincipal userPrincipal) throws LASException {
    BeforeResult<String> beforeResult = hook == null ? new BeforeResult<String>(id, true) : hook.beforeDelete(id, userPrincipal);
    if (!beforeResult.isResult()) return new DeleteResult(beforeResult.getFailMessage());
    try {
      String response = WebUtils.doDelete(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), null);
      LOGGER.info("get response of delete[" + apiAddress + "/" + id + "]:" + response);
      DeleteMsg deleteMsg = LASJsonParser.asObject(response, DeleteMsg.class);
      DeleteResult deleteResult = new DeleteResult(beforeResult, deleteMsg);
      if (hook == null) return deleteResult;
      AfterResult afterResult = hook.afterDelete(beforeResult, deleteMsg, userPrincipal);
      if (!afterResult.isSuccess()) deleteResult.setFailMessage(afterResult.getFailMessage());
      return deleteResult;
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public DeleteResult delete(String[] ids, UserPrincipal userPrincipal) {
    if (ids != null && ids.length > 50) throw new LASException("delete bach max limit 50.");
    try {
      BeforeResult<String[]> beforeResult = hook == null ? new BeforeResult<String[]>(ids, true) : hook.beforeDelete(ids, userPrincipal);
      if (!beforeResult.isResult()) return new DeleteResult(beforeResult.getFailMessage());
      ArrayNode arrays = JsonNodeFactory.instance.arrayNode();
      for (String id : ids) arrays.add(id);
      ObjectNode params = JsonNodeFactory.instance.objectNode();
      params.put("objectIds", arrays);
      String response = WebUtils.doPost(apiAddress + "/delete", CloudCodeContants.getHeaders(userPrincipal), params.toString(), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of deleteBatch[" + apiAddress + "/delete](" + ids + "):" + response);
      return new DeleteResult<String[]>(beforeResult, LASJsonParser.asObject(response, DeleteMsg.class));
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public SaveResult<T> create(T object) throws LASException {
    return this.create(object, null);
  }

  @Override
  public FindMsg<T> find(LASQuery query) throws LASException {
    return this.find(query, null);
  }

  @Override
  public FindMsg<T> find(LASQuery query, boolean count) throws LASException {
    return this.find(query, count, null);
  }

  @Override
  public T findById(String id) throws LASException {
    return this.findById(id, null);
  }

  @Override
  public UpdateMsg update(String id, LASUpdate update) throws LASException {
    return this.update(id, update, null);
  }

  @Override
  public DeleteResult delete(String id) throws LASException {
    return this.delete(id, null);
  }

  @Override
  public DeleteResult delete(String[] ids) throws LASException {
    return this.delete(ids, null);
  }

  String serializeLasQueryForPostQuest(LASQuery lasQuery) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (lasQuery.query() != null) map.put("where", LASJsonParser.asJson(lasQuery.query()));
    if (lasQuery.sort() != null) map.put("order", lasQuery.sort());
    if (lasQuery.keys() != null) map.put("keys", lasQuery.keys());
    if (lasQuery.includes() != null) map.put("include", lasQuery.includes());
    map.put("limit", lasQuery.limit());
    map.put("skip", lasQuery.skip());
//    map.put("excludeKeys", null); Unsupported.
    return LASJsonParser.asJson(map);
  }

}
