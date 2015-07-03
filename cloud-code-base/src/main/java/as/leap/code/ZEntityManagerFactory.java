package as.leap.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * stream
 */
public final class ZEntityManagerFactory {

  private static Map<Class, ZEntityManager> managerMap = new ConcurrentHashMap<Class, ZEntityManager>();

  public static void putManager(Class clazz, ZEntityManager entityManager) {
    managerMap.put(clazz, entityManager);
  }

  public static ZEntityManager getManager(Class clazz) {
    return managerMap.get(clazz);
  }

}
