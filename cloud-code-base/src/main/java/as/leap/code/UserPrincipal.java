package as.leap.code;

/**
 * User：poplar
 * Date：15/9/17
 */
public class UserPrincipal {
  private String appId;
  private IdentityType identityType;
  private String id;
  private String sessionToken;
  private String key;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public IdentityType getIdentityType() {
    return identityType;
  }

  public void setIdentityType(IdentityType identityType) {
    this.identityType = identityType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSessionToken() {
    return sessionToken;
  }

  public void setSessionToken(String sessionToken) {
    this.sessionToken = sessionToken;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return "UserPrincipal{" +
        "appId='" + appId + '\'' +
        ", identityType=" + identityType +
        ", id='" + id + '\'' +
        ", sessionToken='" + sessionToken + '\'' +
        ", key='" + key + '\'' +
        '}';
  }
}
