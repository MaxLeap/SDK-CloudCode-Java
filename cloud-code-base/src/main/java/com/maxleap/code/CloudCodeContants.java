package com.maxleap.code;

import com.maxleap.code.impl.GlobalConfig;

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

  public final static String CONTENT_TYPE = "Content-Type";
  public final static String APPLICATION_JSON = "application/json";
  public final static String HEADER_ML_APPID = "X-ML-AppId";
  public final static String HEADER_ML_MASTERKEY = "X-ML-MasterKey";
  public static final String HEADER_ML_SESSIONTOKEN = "X-ML-Session-Token";
  public static final String HEADER_ML_APIKEY = "X-ML-APIKey";
  public final static String HEADER_ML_REQUEST_FROM_CLOUDCODE = "X-ML-Request-From-Cloudcode";
  public static String DEFAULT_API_ADDRESS_PREFIX = "http://api.las/2.0";
  public final static int DEFAULT_TIMEOUT = 5000;
  public final static int DEFAULT_READ_TIMEOUT = 15000;
  public static GlobalConfig GLOBAL_CONFIG;
  public static Properties LEAP_CONFIG;
  public static Map<String, String> HEADERS = new HashMap<String, String>();

  public static void init() throws MLException {
    GLOBAL_CONFIG = loadGlobalConfig();
    LEAP_CONFIG = loadLeapConfig();
    DEFAULT_API_ADDRESS_PREFIX = LEAP_CONFIG.getProperty("url.leap", "http://api.las/2.0");
    initBaseHeader();
  }

  private static GlobalConfig loadGlobalConfig() {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/global.json");
    if (inputStream == null) throw new MLException("you must have a global.json config file for your cloud code.");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      StringBuilder globalBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) globalBuilder.append(line).append("\n");
      return new GlobalConfig(globalBuilder.toString());
    } catch (MLException e) {
      throw new MLException("Your global.json config is not match json format.Please check your config. Caused by " + e.getMessage());
    } catch (IOException e) {
      throw new MLException(e);
    }
  }

  private static Properties loadLeapConfig() {
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

  private static void initBaseHeader() {
    HEADERS.put(CONTENT_TYPE, APPLICATION_JSON);
    HEADERS.put(HEADER_ML_APPID, GLOBAL_CONFIG.getApplicationID());
//    HEADERS.put(CloudCodeContants.HEADER_ML_MASTERKEY, GLOBAL_CONFIG.getApplicationKey());
    HEADERS.put(HEADER_ML_REQUEST_FROM_CLOUDCODE, "true");
  }

  public static Map<String, String> getHeaders(UserPrincipal userPrincipal) {
    Map<String, String> headers = new HashMap<String, String>();
    headers.putAll(CloudCodeContants.HEADERS);
    if (userPrincipal == null) {
      headers.put(HEADER_ML_MASTERKEY, GLOBAL_CONFIG.getApplicationKey());
    } else {
      switch (userPrincipal.getIdentityType()) {
        case API_KEY:
          headers.put(HEADER_ML_APIKEY, userPrincipal.getKey());
          break;
        case MASTER_KEY:
          headers.put(HEADER_ML_MASTERKEY, userPrincipal.getKey());
          break;
        case APP_USER:
        case ORG_USER:
          headers.put(HEADER_ML_SESSIONTOKEN, userPrincipal.getSessionToken());
          break;
      }
    }
    return headers;
  }
}
