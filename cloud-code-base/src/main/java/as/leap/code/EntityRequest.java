package as.leap.code;

/**
 *
 */
public class EntityRequest {

  //method name
  private String method;

  //json string
  private String params;

  public EntityRequest() {
  }

  public EntityRequest(String method, String params) {
    this.method = method;
    this.params = params;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  @Override
  public String toString() {
    return "EntityRequest{" +
        "method='" + method + '\'' +
        ", params='" + params + '\'' +
        '}';
  }
}
