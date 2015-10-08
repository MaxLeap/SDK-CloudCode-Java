package as.leap.code.impl;

import as.leap.code.Request;
import as.leap.code.UserPrincipal;

import java.util.List;
import java.util.Map;

/**
 * Created by stream .
 */
public class LASRequest implements Request {

  //参数
  private String params;
  //用户信息
  private UserPrincipal userPrincipal;

  public LASRequest() {
  }

  public LASRequest(String params, UserPrincipal userPrincipal) {
    this.params = params;
    this.userPrincipal = userPrincipal;
  }

  public Object getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  @Override
  public UserPrincipal getUserPrincipal() {
    return userPrincipal;
  }

  public void setUserPrincipal(UserPrincipal userPrincipal) {
    this.userPrincipal = userPrincipal;
  }

  @Override
  public <T> T parameter(Class<T> clazz) {
    if (params == null) return null;
    T value;

    if (clazz.equals(String.class)) {
      value = (T) params;
    } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
      value = (T) Integer.valueOf(params.toString());
    } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
      value = (T) Float.valueOf(params.toString());
    } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
      value = (T) Long.valueOf(params.toString());
    } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
      value = (T) Boolean.valueOf(params.toString());
    } else if (clazz.equals(Void.class)) {
      value = null;
    } else if (clazz.equals(List.class)) {
      value = (T) LASJsonParser.asList(params.toString());
    } else if (clazz.equals(Map.class)) {
      value = (T) LASJsonParser.asMap(params.toString());
    } else {
      try {
        value = LASJsonParser.asObject(params.toString(), clazz);
      } catch (Exception e) {
        throw new IllegalArgumentException("can not convert parameter to Java Object. " + params, e);
      }
    }
    return value;
  }

}
