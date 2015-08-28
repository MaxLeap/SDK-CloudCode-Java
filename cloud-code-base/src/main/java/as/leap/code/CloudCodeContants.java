package as.leap.code;

import as.leap.code.impl.GlobalConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User：poplar
 * Date：15/8/26
 */
public class CloudCodeContants {

  public final static String HEADER_ZCLOUD_APPID = "X-ZCloud-AppId";
  public final static String HEADER_ZCLOUD_MASTERKEY = "X-ZCloud-MasterKey";
  public final static String HEADER_ZCLOUD_REQUEST_FROM_CLOUDCODE = "X-ZCloud-Request-From-Cloudcode";
  public static String DEFAULT_API_ADDRESS_PREFIX = "http://apiuat.zcloud.io/2.0";
  public final static int DEFAULT_TIMEOUT = 5000;
  public final static int DEFAULT_READ_TIMEOUT = 15000;
  public static GlobalConfig GLOBAL_CONFIG;
  public static Properties LEAP_CONFIG;
  public static Map<String,String> HEADERS = new HashMap<String, String>();

  public static void init() throws LASException{
    GLOBAL_CONFIG = loadGlobalConfig();
    LEAP_CONFIG = loadLeapConfig();
    DEFAULT_API_ADDRESS_PREFIX = LEAP_CONFIG.getProperty("url.leap", "http://apiuat.zcloud.io/2.0");
    initHeader();
  }

  private static GlobalConfig loadGlobalConfig() {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/global.json");
    if (inputStream == null) throw new LASException("you must have a global.json config file for your cloud code.");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      StringBuilder globalBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) globalBuilder.append(line).append("\n");
      return new GlobalConfig(globalBuilder.toString());
    } catch (LASException e) {
      throw new LASException("Your global.json config is not match json format.Please check your config. Caused by "+ e.getMessage());
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  private static Properties loadLeapConfig(){
    Properties properties = new Properties();
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("leap-config.properties");
    if (inputStream == null) return properties;
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

  private static void initHeader() {
    HEADERS.put(CloudCodeContants.HEADER_ZCLOUD_APPID, GLOBAL_CONFIG.getApplicationID());
    HEADERS.put(CloudCodeContants.HEADER_ZCLOUD_MASTERKEY, GLOBAL_CONFIG.getApplicationKey());
    HEADERS.put(CloudCodeContants.HEADER_ZCLOUD_REQUEST_FROM_CLOUDCODE, "true");
  }
}
