package com.maxleap.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * stream
 */
public final class MLClassManagerFactory {

  private static Map<Class<?>, MLClassManager> managerMap = new ConcurrentHashMap<Class<?>, MLClassManager>();

  public static <T> void putManager(Class<T> clazz, MLClassManager<T> entityManager) {
    managerMap.put(clazz, entityManager);
  }

  public static Map<Class<?>, MLClassManager> getManagerMap() {
    return managerMap;
  }

  public static <T> MLClassManager<T> getManager(Class<T> clazz) {
    return (MLClassManager<T>) managerMap.get(clazz);
  }

}
