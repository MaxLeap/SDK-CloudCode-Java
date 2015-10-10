package com.maxleap.code.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.maxleap.code.LASException;

import java.io.IOException;
import java.util.*;

/**
 * Created by stream.
 */
public abstract class LASJsonParser {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

  static {
    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

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

  public static JsonNode asJsonNode(Object obj) {
    return asJsonNode(asJson(obj));
  }

  public static Map<String, Object> objectToMap(Object object) {
    if (object == null) return null;
    try {
      String jsonStr = mapper.writeValueAsString(object);
      return mapper.readValue(jsonStr, Map.class);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  public static List asList(String json) {
    return mapper.convertValue(asJsonNode(json), List.class);
  }

  public static Map<String, Object> asMap(String json) {
    return mapper.convertValue(asJsonNode(json), Map.class);
  }

  public static Map<String, Object> jsonNodeToMap(JsonNode jsonNode) {
    return mapper.convertValue(jsonNode, Map.class);
  }

  public static <T> String asJson(T obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new LASException(e);
    }
  }
}
