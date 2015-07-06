package as.leap.code.impl;

import as.leap.code.*;
import as.leap.code.assist.AssistLASClassManager;
import as.leap.code.assist.Path;
import as.leap.las.sdk.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * User：poplar
 * Date：15-6-2
 */
public class AssistLASClassManagerImpl<T> implements AssistLASClassManager<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssistLASClassManagerImpl.class);

  private String appId;
  private String masterKey;

  private Class<T> tClass;

  private String apiAddress;

  private String path;

  final static int DEFAULT_TIMEOUT = 5000;
  final static int DEFAULT_READ_TIMEOUT = 15000;
  public static String DEFAULT_API_ADDRESS_PREFIX = "http://apiuat.zcloud.io/2.0";
  private final static String HEADER_ZCLOUD_APPID = "X-ZCloud-AppId";
  private final static String HEADER_ZCLOUD_MASTERKEY = "X-ZCloud-MasterKey";
  private final static String HEADER_ZCLOUD_REQUEST_FROM_CLOUDCODE = "X-ZCloud-Request-From-Cloudcode";

  public AssistLASClassManagerImpl(Class<T> tClass) {
    this.tClass = tClass;
    this.path = this.tClass.getAnnotation(Path.class).value();
    GlobalConfig globalConfig = getGlabalConfig();
    this.appId = globalConfig.getApplicationID();
    this.masterKey = globalConfig.getApplicationKey();
    this.apiAddress = DEFAULT_API_ADDRESS_PREFIX;
  }

  String getAPIAddress() {
    return apiAddress + path;
  }

  @Override
  public FindMsg<T> find(LASQuery query) {
    try {
      String response = WebUtils.doPost(getAPIAddress() + "/query", getHeader(), serializeLasQueryForPostQuest(query), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of find[" + getAPIAddress() + "/query]:" + response);
      JsonNode jsonNode = LASJsonParser.asJsonNode(response);
      ArrayNode results = (ArrayNode) jsonNode.get("results");
      int c = 0;
      List<T> r = new ArrayList<T>();
      if (results != null) {
        c = results.size();
        for (JsonNode result : results) {
          r.add(LASJsonParser.asObject(result.toString(), tClass));
        }
      }
      return new FindMsg<T>(c, r);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public SaveMsg create(T obj) throws as.leap.code.LASException {
    try {
      String response = WebUtils.doPost(getAPIAddress(), getHeader(), LASJsonParser.asJson(obj), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of create[" + getAPIAddress() + "]:" + response);
      return LASJsonParser.asObject(response, SaveMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public T findById(String id) {
    try {
      String response = WebUtils.doGet(getAPIAddress() + "/" + id, getHeader(), null);
      LOGGER.info("get response of findById[" + getAPIAddress() + "/" + id + "]:" + response);
      if ("{}".equals(response)) return null;
      return LASJsonParser.asObject(response, tClass);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public UpdateMsg update(String id, LASUpdate update) throws as.leap.code.LASException {
    try {
      String response = WebUtils.doPut(getAPIAddress() + "/" + id, getHeader(), LASJsonParser.asJson(update.update()), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of update[" + getAPIAddress() + "/" + id + "](" + update.update() + "):" + response);
      return LASJsonParser.asObject(response, UpdateMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  @Override
  public DeleteMsg delete(String id) {
    try {
      String response = WebUtils.doDelete(getAPIAddress() + "/" + id, getHeader(), null);
      LOGGER.info("get response of delete[" + getAPIAddress() + "/" + id + "]:" + response);
      return LASJsonParser.asObject(response, DeleteMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
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

  Map<String, String> getHeader() {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put(HEADER_ZCLOUD_APPID, appId);
    headers.put(HEADER_ZCLOUD_MASTERKEY, masterKey);
    headers.put(HEADER_ZCLOUD_REQUEST_FROM_CLOUDCODE, "true");
    return headers;
  }

  private GlobalConfig getGlabalConfig() {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/global.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder globalBuilder = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) globalBuilder.append(line);
    } catch (IOException e) {
      e.printStackTrace();
    }
    GlobalConfig globalConfig = new GlobalConfig(globalBuilder.toString());
    return globalConfig;
  }
}
