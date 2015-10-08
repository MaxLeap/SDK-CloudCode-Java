package as.leap.code.impl;

import as.leap.code.IdentityType;
import as.leap.code.MethodName;
import as.leap.code.Request;
import as.leap.code.UserPrincipal;
import as.leap.las.sdk.LASUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * User：poplar
 * Date：15/9/17
 */
public class LASClassManagerRequest implements Request {

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
    return "LASEntityManagerRequest{" +
        "params='" + params + '\'' +
        ", userPrincipal=" + userPrincipal +
        ", method=" + method.getMethodName() +
        '}';
  }

  public <K> LASClassManagerCreateRequest<K> asCreateRequest(Class<K> entityClazz){
    K entity = LASJsonParser.asObject(LASJsonParser.asJson(params), entityClazz);
    return new LASClassManagerCreateRequest<K>(entity,userPrincipal);
  }

  public LASClassManagerUpdateRequest asUpdateRequest(){
    Map<String,Object> map = (Map)params;
    String objectId = map.get("objectId").toString();
    Map<String,Object> update = (Map)map.get("update");
    return new LASClassManagerUpdateRequest(objectId,update);
  }

  public LASClassManagerDeleteRequest asDeleteRequest(){
    return new LASClassManagerDeleteRequest(params.toString());
  }

  public LASClassManagerDeleteBatchRequest asDeleteBatchRequest(){
    List<String> objectIds = (List)params;
    return new LASClassManagerDeleteBatchRequest(objectIds.toArray(new String[objectIds.size()]));
  }

  public class LASClassManagerCreateRequest<K>{
    private K entity;
    private UserPrincipal userPrincipal;

    public LASClassManagerCreateRequest(K entity, UserPrincipal userPrincipal) {
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

  public class LASClassManagerUpdateRequest{
    private String objectId;
    private Map<String,Object> update;

    public LASClassManagerUpdateRequest(String objectId, Map<String,Object> update) {
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

    public LASUpdate lasUpdate(){
      return LASUpdate.getUpdate(update);
    }

  }

  public class LASClassManagerDeleteRequest{
    private String objectId;

    public LASClassManagerDeleteRequest(String objectId) {
      this.objectId = objectId;
    }

    public String getObjectId() {
      return objectId;
    }
  }

  public class LASClassManagerDeleteBatchRequest{
    private String[] objectIds;

    public LASClassManagerDeleteBatchRequest(String[] objectIds) {
      this.objectIds = objectIds;
    }

    public String[] getObjectIds() {
      return objectIds;
    }
  }

  public static void main(String[] args){
    LASClassManagerRequest request = new LASClassManagerRequest();
    request.setMethod(MethodName.CREATE);
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setAppId("aa");
    userPrincipal.setIdentityType(IdentityType.ORG_USER);
    userPrincipal.setSessionToken("sessionTokenTest");
    request.setUserPrincipal(userPrincipal);
    request.setParams("{\"a\":123}");
    String json = LASJsonParser.asJson(request);
    System.out.println(json);

    String json2 = "{\"params\":{\"a\":123},\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";
    String json3 = "{\"params\":\"123\",\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";
    String json4 = "{\"params\":[\"123\",\"234\"],\"userPrincipal\":{\"appId\":\"aa\",\"identityType\":\"ORG_USER\",\"id\":null,\"sessionToken\":\"sessionTokenTest\",\"key\":null},\"method\":\"create\"}";

    String json5 = "{\"params\":{\"update\":{\"name\":\"234\"},\"objectId\":\"56010ba960b255d4587e49c3\"},\"userPrincipal\":{\"identityType\":\"MASTER_KEY\",\"appId\":\"55598fd560b2f98aa901b619\",\"id\":null,\"sessionToken\":null,\"key\":\"dWhNN2V0eVlYNTdBSDF2elJKUHhpUQ\"},\"method\":\"update\"}";
    request = LASJsonParser.asObject(json5,LASClassManagerRequest.class);
    System.out.println(LASJsonParser.asJson(request.asUpdateRequest()));
  }
}
