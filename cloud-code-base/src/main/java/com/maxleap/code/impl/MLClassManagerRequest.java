package com.maxleap.code.impl;

import com.maxleap.code.IdentityType;
import com.maxleap.code.MethodName;
import com.maxleap.code.Request;
import com.maxleap.code.UserPrincipal;
import com.maxleap.las.sdk.MLUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * User：poplar
 * Date：15/9/17
 */
public class MLClassManagerRequest implements Request {

  //请求参数
  private Object params;
  //用户信息
  @JsonIgnore
  private UserPrincipal userPrincipal;
  //请求METHOD
  private MethodName method;

  public Object getParams() {
    return params;
  }

  public void setParams(Object params) {
    this.params = params;
  }

  public UserPrincipal getUserPrincipal() {
    return userPrincipal;
  }

  public void setUserPrincipal(UserPrincipal userPrincipal) {
    this.userPrincipal = userPrincipal;
  }

  @JsonProperty
  public String getMethod() {
    return method.getMethodName();
  }

  @JsonIgnore
  public MethodName getMethodName() {
    return method;
  }

  @JsonProperty
  public void setMethod(String methodName) {
    this.method = MethodName.getByMethodName(methodName);
  }

  public void setMethod(MethodName method) {
    this.method = method;
  }

  @Override
  public <T> T parameter(Class<T> clazz) {
    return null;
  }

  @Override
  public String toString() {
    return "MLEntityManagerRequest{" +
        "params='" + params + '\'' +
        ", userPrincipal=" + userPrincipal +
        ", method=" + method.getMethodName() +
        '}';
  }

  public <K> MLClassManagerCreateRequest<K> asCreateRequest(Class<K> entityClazz) {
    K entity = MLJsonParser.asObject(MLJsonParser.asJson(params), entityClazz);
    return new MLClassManagerCreateRequest<K>(entity, userPrincipal);
  }

  public MLClassManagerUpdateRequest asUpdateRequest() {
    Map<String, Object> map = (Map) params;
    String objectId = map.get("objectId").toString();
    Map<String, Object> update = (Map) map.get("update");
    return new MLClassManagerUpdateRequest(objectId, update);
  }

  public MLClassManagerDeleteRequest asDeleteRequest() {
    return new MLClassManagerDeleteRequest(params.toString());
  }

  public MLClassManagerDeleteBatchRequest asDeleteBatchRequest() {
    List<String> objectIds = (List) params;
    return new MLClassManagerDeleteBatchRequest(objectIds.toArray(new String[objectIds.size()]));
  }

  public class MLClassManagerCreateRequest<K> {
    private K entity;
    private UserPrincipal userPrincipal;

    public MLClassManagerCreateRequest(K entity, UserPrincipal userPrincipal) {
      this.entity = entity;
      this.userPrincipal = userPrincipal;
    }

    public K getEntity() {
      return entity;
    }

    public UserPrincipal getUserPrincipal() {
      return userPrincipal;
    }
  }

  public class MLClassManagerUpdateRequest {
    private String objectId;
    private Map<String, Object> update;

    public MLClassManagerUpdateRequest(String objectId, Map<String, Object> update) {
      this.objectId = objectId;
      this.update = update;
    }

    public String getObjectId() {
      return objectId;
    }

    public void setObjectId(String objectId) {
      this.objectId = objectId;
    }

    public Map<String, Object> getUpdate() {
      return update;
    }

    public void setUpdate(Map<String, Object> update) {
      this.update = update;
    }

    public MLUpdate lasUpdate() {
      return MLUpdate.getUpdate(update);
    }

  }

  public class MLClassManagerDeleteRequest {
    private String objectId;

    public MLClassManagerDeleteRequest(String objectId) {
      this.objectId = objectId;
    }

    public String getObjectId() {
      return objectId;
    }
  }

  public class MLClassManagerDeleteBatchRequest {
    private String[] objectIds;

    public MLClassManagerDeleteBatchRequest(String[] objectIds) {
      this.objectIds = objectIds;
    }

    public String[] getObjectIds() {
      return objectIds;
    }
  }

  public static void main(String[] args) {
    MLClassManagerRequest request = new MLClassManagerRequest();
    request.setMethod(MethodName.CREATE);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setAppId("aa");
    userPrincipal.setIdentityType(IdentityType.ORG_USER);
    userPrincipal.setSessionToken("sessionTokenTest");
    request.setUserPrincipal(userPrincipal);
    request.setParams("{\"a\":123}");
    String json = MLJsonParser.asJson(request);
    System.out.println(json);

    String json2 = "{\"params\":{\"a\":123},\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";
    String json3 = "{\"params\":\"123\",\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";
    String json4 = "{\"params\":[\"123\",\"234\"],\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";

    String json5 = "{\"params\":{\"update\":{\"name\":\"234\"},\"objectId\":\"56010ba960b255d4587e49c3\"},\"userPrincipal\":{\"identityType\":\"MASTER_KEY\",\"appId\":\"55598fd560b2f98aa901b619\",\"id\":null,\"sessionToken\":null,\"key\":\"dWhNN2V0eVlYNTdBSDF2elJKUHhpUQ\"},\"method\":\"update\"}";
    request = MLJsonParser.asObject(json5, MLClassManagerRequest.class);
    System.out.println(MLJsonParser.asJson(request.asUpdateRequest()));
  }
}
