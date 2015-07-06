package as.leap.code.impl;

import as.leap.code.Request;

import java.util.Map;

/**
 * Created by stream .
 */
public class LASRequest implements Request {

  private String originParams;
  private boolean useMasterKey;

  public LASRequest(String originParams) {
    this.originParams = originParams;
  }

  @Override
  public <T> T parameter(Class<T> clazz) {
    if (originParams == null) return null;
    T value;
    if (clazz.equals(String.class)) {
      value = (T) originParams;
    } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
      value = (T) Integer.valueOf(originParams);
    } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
      value = (T) Float.valueOf(originParams);
    } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
      value = (T) Long.valueOf(originParams);
    } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
      value = (T) Boolean.valueOf(originParams);
    } else if (clazz.equals(Void.class)) {
      value = null;
    } else if (clazz.equals(Map.class)) {
      value = (T) LASJsonParser.jsonNodeToMap(LASJsonParser.asJsonNode(originParams));
    } else {
      try {
        value = LASJsonParser.asObject(originParams, clazz);
      } catch (Exception e) {
        throw new IllegalArgumentException("can not convert parameter to Java Object. " + originParams, e);
      }
    }
    return value;
  }

  @Override
  public boolean isUseMasterKey() {
    return useMasterKey;
  }

  @Override
  public void useMasterKey() {
    this.useMasterKey = true;
  }

}
