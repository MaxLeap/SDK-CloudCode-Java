package as.leap.code.impl;

import as.leap.code.*;
import as.leap.las.sdk.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for entity request.
 */
public final class LASClassManagerHandler<T> implements LASHandler<Request, Response> {

  private static final Logger logger = LoggerFactory.getLogger(LASClassManagerHandler.class);

  private final LASClassManager<T> entityManager;
  private final Class<T> entityClazz;
  private final LASClassManagerHook hook;

  private static final String PARAMS = "params";

  private enum MethodName {
    CREATE("create"), UPDATE("update"), DELETE("delete"), DELETEBATCH("deleteBatch"),
    FIND_BY_ID("findById"), FIND("find"), UN_KNOW("unKnow");

    private String methodName;

    MethodName(String name) {
      this.methodName = name;
    }

    public static MethodName getByMethodName(String methodName) {
      for (MethodName method : values()) {
        if (method.methodName.equals(methodName))
          return method;
      }
      return UN_KNOW;
    }
  }

  public LASClassManagerHandler(LASClassManager<T> entityManager, LASClassManagerHook hook, Class<T> entityClazz) {
    this.entityManager = entityManager;
    this.hook = hook;
    this.entityClazz = entityClazz;
  }

  @Override
  public Response handle(Request request) {
    JsonNode jsonNode = LASJsonParser.asJsonNode(request.parameter(String.class));
    String methodName = jsonNode.get("method").textValue();
    MethodName method = MethodName.getByMethodName(methodName);
    switch (method) {
      case CREATE:
        Response<SaveMsg> createResponse = new LASResponse(SaveMsg.class);
        JsonNode entityJsonObject = jsonNode.get(PARAMS);
        try {
          T entity = LASJsonParser.asObject(entityJsonObject.toString(), entityClazz);
          SaveResult saveResult = entityManager.create(entity);
          if (saveResult.isSuccess()) {
            createResponse.setResult(saveResult.getSaveMessage());
          } else {
            createResponse.setError(saveResult.getFailMessage());
          }
        } catch (Exception e) {
          createResponse.setError(e.getMessage());
        }
        return createResponse;
      case UPDATE:
        Response<UpdateMsg> updateResponse = new LASResponse<UpdateMsg>(UpdateMsg.class);
        JsonNode params = jsonNode.get(PARAMS);
        String objectID = params.get("objectId").textValue();
        JsonNode updateJsonObject = params.get("update");
        try {
          UpdateMsg updateMsg = entityManager.update(objectID, LASUpdate.getUpdate(LASJsonParser.jsonNodeToMap(updateJsonObject)));
          updateResponse.setResult(updateMsg);
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          updateResponse.setError(e.getMessage());
        }
        return updateResponse;
      case DELETE:
        Response<DeleteMsg> deleteResponse = new LASResponse(DeleteMsg.class);
        try {
          DeleteResult deleteResult = entityManager.delete(jsonNode.get(PARAMS).textValue());
          if (deleteResult.isSuccess()) {
            deleteResponse.setResult(deleteResult.getDeleteMessage());
          } else {
            deleteResponse.setError(deleteResult.getFailMessage());
          }
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          deleteResponse.setError(e.getMessage());
        }
        return deleteResponse;
      case DELETEBATCH:
        Response<DeleteMsg> deleteBatchResponse = new LASResponse(DeleteMsg.class);

        JsonNode deleteBatchParams = jsonNode.get(PARAMS);
        String[] array = new String[deleteBatchParams.size()];
        for (int i = 0; i < deleteBatchParams.size(); i++) {
          array[i] = deleteBatchParams.get(i).textValue();
        }
        try {
          DeleteResult deleteResult = entityManager.delete(array);
          deleteBatchResponse.setResult(deleteResult.getDeleteMessage());
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          deleteBatchResponse.setError(e.getMessage());
        }
        return deleteBatchResponse;
      case FIND_BY_ID:
        Response findByIdResponse = new LASResponse(entityClazz);
        try {
          T findByIdResult = entityManager.findById(jsonNode.get(PARAMS).textValue());
          findByIdResponse.setResult(findByIdResult);
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          findByIdResponse.setError(e.getMessage());
        }
        return findByIdResponse;
      case FIND:
        JsonNode queryObject = jsonNode.get(PARAMS);
        String keys = queryObject.get("keys").textValue();
        String include = queryObject.get("includes").textValue();
        int limit = queryObject.get("limit").intValue();
        int skip = queryObject.get("skip").intValue();
        Integer type = queryObject.get("type").intValue();

        String order = queryObject.get("order").textValue();
        JsonNode query = queryObject.get("query");

        LASQuery lasQuery = null;
        List<String> keys1 = null;
        if (keys != null) {
          keys1 = Arrays.asList(keys.split(","));
        }
        if (query != null) {
          lasQuery = new LASQuery(LASJsonParser.jsonNodeToMap(query), keys1);
        } else {
          lasQuery = new LASQuery(new HashMap(), keys1);
        }

        if (order != null) {
          Map<String, Integer> orderMap = new HashMap<String, Integer>();
          for (String temp : order.split(",")) {
            if (temp.startsWith("-")) {
              orderMap.put(temp.substring(1), -1);
            } else {
              orderMap.put(temp, 1);
            }
          }
          lasQuery.setSort(orderMap);
        }

        lasQuery.setSkip(skip);
        lasQuery.setLimit(limit);

        lasQuery.setIncludes(include);

        Response findResponse = new LASResponse(FindMsg.class);

        try {
          FindMsg<T> ob = entityManager.find(lasQuery, type != null && type == 4);
          findResponse.setResult(ob);
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          findResponse.setError(e.getMessage());
        }
        return findResponse;
      default:
        throw new IllegalStateException("the method can not be matched. " + methodName);
    }
  }
}