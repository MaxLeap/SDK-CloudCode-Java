package as.leap.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * stream
 */
public final class LASClassManagerFactory {

  private static Map<Class, LASClassManager> managerMap = new ConcurrentHashMap<Class, LASClassManager>();

  public static void putManager(Class clazz, LASClassManager entityManager) {
    managerMap.put(clazz, entityManager);
  }

  public static LASClassManager getManager(Class clazz) {
    return managerMap.get(clazz);
  }

}
