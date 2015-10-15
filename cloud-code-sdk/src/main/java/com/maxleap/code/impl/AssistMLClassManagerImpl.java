package com.maxleap.code.impl;

import com.maxleap.code.*;
import com.maxleap.code.MLException;
import com.maxleap.code.assist.AssistMLClassManager;
import com.maxleap.code.assist.Path;
import com.maxleap.las.sdk.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

/**
 * User：poplar
 * Date：15-6-2
 */
public class AssistMLClassManagerImpl<T> implements AssistMLClassManager<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssistMLClassManagerImpl.class);
  private Class<T> tClass;
  String apiAddress;

  public AssistMLClassManagerImpl(Class<T> tClass) {
    this.tClass = tClass;
    String path = this.tClass.getAnnotation(Path.class).value();
    this.apiAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + path;
  }

  @Override
  public FindMsg<T> find(MLQuery query, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/query", CloudCodeContants.getHeaders(userPrincipal), serializeLasQueryForPostQuest(query), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of find[" + apiAddress + "/query]:" + response);
      JsonNode jsonNode = MLJsonParser.asJsonNode(response);
      ArrayNode results = (ArrayNode) jsonNode.get("results");
      int c = 0;
      List<T> r = new ArrayList<T>();
      if (results != null) {
        c = results.size();
        for (JsonNode result : results) {
          r.add(MLJsonParser.asObject(result.toString(), tClass));
        }
      }
      return new FindMsg<T>(c, r);
    } catch (Exception e) {
      throw new com.maxleap.code.MLException(e);
    }
  }

  @Override
  public SaveMsg create(T obj, UserPrincipal userPrincipal) throws MLException {
    try {
      String response = WebUtils.doPost(apiAddress, CloudCodeContants.getHeaders(userPrincipal), MLJsonParser.asJson(obj), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of create[" + apiAddress + "]:" + response);
      return MLJsonParser.asObject(response, SaveMsg.class);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  @Override
  public T findById(String id, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doGet(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), null);
      LOGGER.info("get response of findById[" + apiAddress + "/" + id + "]:" + response);
      if ("{}".equals(response)) return null;
      return MLJsonParser.asObject(response, tClass);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  @Override
  public UpdateMsg update(String id, MLUpdate update, UserPrincipal userPrincipal) throws MLException {
    try {
      String response = WebUtils.doPut(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), MLJsonParser.asJson(update.update()), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of update[" + apiAddress + "/" + id + "](" + update.update() + "):" + response);
      return MLJsonParser.asObject(response, UpdateMsg.class);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  @Override
  public DeleteMsg delete(String id, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doDelete(apiAddress + "/" + id, CloudCodeContants.getHeaders(userPrincipal), null);
      LOGGER.info("get response of delete[" + apiAddress + "/" + id + "]:" + response);
      return MLJsonParser.asObject(response, DeleteMsg.class);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  @Override
  public SaveMsg create(T coin) {
    return this.create(coin, null);
  }

  @Override
  public T findById(String id) {
    return this.findById(id, null);
  }

  @Override
  public UpdateMsg update(String id, MLUpdate update) {
    return this.update(id, update, null);
  }

  @Override
  public DeleteMsg delete(String id) {
    return this.delete(id, null);
  }

  @Override
  public FindMsg<T> find(MLQuery query) {
    return this.find(query, null);
  }

  String serializeLasQueryForPostQuest(MLQuery lasQuery) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (lasQuery.query() != null) map.put("where", MLJsonParser.asJson(lasQuery.query()));
    if (lasQuery.sort() != null) map.put("order", lasQuery.sort());
    if (lasQuery.keys() != null) map.put("keys", lasQuery.keys());
    if (lasQuery.includes() != null) map.put("include", lasQuery.includes());
    map.put("limit", lasQuery.limit());
    map.put("skip", lasQuery.skip());
//    map.put("excludeKeys", null); Unsupported.
    return MLJsonParser.asJson(map);
  }

}
