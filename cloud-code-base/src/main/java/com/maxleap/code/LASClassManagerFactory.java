package com.maxleap.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * stream
 */
public final class LASClassManagerFactory {

  private static Map<Class<?>, LASClassManager> managerMap = new ConcurrentHashMap<Class<?>, LASClassManager>();

  public static <T> void putManager(Class<T> clazz, LASClassManager<T> entityManager) {
    managerMap.put(clazz, entityManager);
  }

  public static <T> LASClassManager<T> getManager(Class<T> clazz) {
    return (LASClassManager<T>) managerMap.get(clazz);
  }

}
