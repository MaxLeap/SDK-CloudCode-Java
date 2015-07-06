package as.leap.code.impl;

import as.leap.code.LASException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 */
public class GlobalConfig {

  private String applicationName;
  private String applicationID;
  private String applicationKey;
  private String packageHook;
  private String packageEntity;
  private String codeMain;
  private String version;

  private String configJsonStr;

  public GlobalConfig() {
  }

  public GlobalConfig(String jsonStr) {
    this(LASJsonParser.asJsonNode(jsonStr));
    this.configJsonStr = jsonStr;
  }

  public GlobalConfig(JsonNode jsonNode) {
    if (!jsonNode.has("applicationId")) throw new LASException("Your applicationId is miss.Please check your global.json config.");
    applicationID = jsonNode.get("applicationId").asText();
    if (applicationID == null || applicationID.trim().equals("")) throw new LASException("Your applicationId is empty.Please check your global.json config.");

    if (!jsonNode.has("applicationKey")) throw new LASException("Your applicationKey is miss.Please check your global.json config.");
    applicationKey = jsonNode.get("applicationKey").asText();
    if (applicationKey == null || applicationKey.trim().equals("")) throw new LASException("Your applicationKey is empty.Please check your global.json config.");

    if (!jsonNode.has("applicationName")) throw new LASException("Your applicationName is miss.Please check your global.json config.");
    applicationName = jsonNode.get("applicationName").asText();
    if (applicationName == null || applicationName.trim().equals("")) throw new LASException("Your applicationName is empty.Please check your global.json config.");

    if (!jsonNode.has("java-main")) throw new LASException("Your java-main is miss.Please check your global.json config.");
    codeMain = jsonNode.get("java-main").asText();
    if (codeMain == null || codeMain.trim().equals("")) throw new LASException("Your java-main is empty.Please check your global.json config.");

    packageHook = jsonNode.has("package-hook") ? jsonNode.path("package-hook").asText() : null;
    packageEntity = jsonNode.has("package-entity") ? jsonNode.path("package-entity").asText() : null;

    if (!jsonNode.has("global") || !jsonNode.get("global").has("zVersion")) throw new LASException("Your global zVersion is miss.Please check your global.json config.");
    version = jsonNode.get("global").get("zVersion").asText();
    if (version == null || version.trim().equals("")) throw new LASException("Your global zVersion is empty.Please check your global.json config.");
    this.configJsonStr = jsonNode.toString();
  }

  public String getConfigJsonStr() {
    return configJsonStr;
  }

  public void setConfigJsonStr(String configJsonStr) {
    this.configJsonStr = configJsonStr;
  }

  public String getCodeMain() {
    return codeMain;
  }

  public void setCodeMain(String codeMain) {
    this.codeMain = codeMain;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getApplicationID() {
    return applicationID;
  }

  public void setApplicationID(String applicationID) {
    this.applicationID = applicationID;
  }

  public String getApplicationKey() {
    return applicationKey;
  }

  public void setApplicationKey(String applicationKey) {
    this.applicationKey = applicationKey;
  }

  public String getPackageHook() {
    return packageHook;
  }

  public void setPackageHook(String packageHook) {
    this.packageHook = packageHook;
  }

  public String getPackageEntity() {
    return packageEntity;
  }

  public void setPackageEntity(String packageEntity) {
    this.packageEntity = packageEntity;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "GlobalConfig{" +
        "applicationName='" + applicationName + '\'' +
        ", applicationID='" + applicationID + '\'' +
        ", applicationKey='" + applicationKey + '\'' +
        ", packageHook='" + packageHook + '\'' +
        ", packageEntity='" + packageEntity + '\'' +
        ", codeMain='" + codeMain + '\'' +
        ", version='" + version + '\'' +
        '}';
  }
}
