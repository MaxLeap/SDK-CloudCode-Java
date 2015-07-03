package as.leap.code.test.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import as.leap.code.LASException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by stream.
 * TODO: make exception more clearly.
 */
abstract class ZJsonParser {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

  public static <T> T asObject(String source, Class<T> clazz) {
    try {
      return mapper.readValue(source, typeFactory.uncheckedSimpleType(clazz));
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  public static <T> T asObject(String source, JavaType type) {
    try {
      return mapper.readValue(source, type);
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  public static JsonNode asJsonNode(String source) {
    try {
      return mapper.readTree(source);
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

  public static Map<String, Object> jsonNodeToMap(JsonNode jsonNode) {
    if (jsonNode == null) {
      return null;
    }
    Iterator<String> paramsIterator = jsonNode.fieldNames();
    Map<String, Object> map = new HashMap<String, Object>();
    while (paramsIterator.hasNext()) {
      String paramName = paramsIterator.next();
      JsonNode jsonSonNode = jsonNode.get(paramName);
      if (jsonSonNode.isInt()) {
        map.put(paramName, jsonSonNode.asInt());
      } else if (jsonSonNode.isLong()) {
        map.put(paramName, jsonSonNode.asLong());
      } else if (jsonSonNode.isTextual()) {
        map.put(paramName, jsonSonNode.asText());
      } else if (jsonSonNode.isObject()) {
        Map<String, Object> tempMap = jsonNodeToMap(jsonSonNode);
        if (tempMap != null) {
          map.put(paramName, tempMap);
        }
      } else if (jsonSonNode.isArray()) {
        Iterator<JsonNode> nodeIterator = jsonSonNode.elements();
        ArrayList<Map<String, Object>> mapArrayList = new ArrayList<Map<String, Object>>();
        while (nodeIterator.hasNext()) {
          JsonNode tempJsonNode = nodeIterator.next();
          Map<String, Object> tempM = jsonNodeToMap(tempJsonNode);
          if (tempM != null) {
            mapArrayList.add(tempM);
          }
        }
        map.put(paramName, mapArrayList);
      }
    }
    return map;
  }

  public static <T> String asJson(T obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new LASException(e);
    }
  }

  public static JsonNode asJsonNode(Object obj) {
    return asJsonNode(asJson(obj));
  }

}
