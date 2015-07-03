package as.leap.code.test.framework;

import as.leap.code.*;
import as.leap.code.impl.*;
import as.leap.code.impl.ZJsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
class BootstrapZCloud {

  private GlobalConfig globalConfig;
  private static final Logger logger = LoggerFactory.getLogger(BootstrapZCloud.class);
  private LASLoaderBase loader;
  private ClassLoader classLoader;
  private Set<Class> hookEntities;
  private Map<String, ZEntityManagerHandler> entityManagerHandlerMap = new ConcurrentHashMap<String, ZEntityManagerHandler>();
  private String restAddr;

  public void setRestAddr(String restAddr) {
    this.restAddr = restAddr;
  }

  public void start() throws Exception {
    globalConfig = loadConfig();
    hookEntities = new HashSet<Class>();
    if (globalConfig.getPackageHook() != null && !isBlank(globalConfig.getPackageHook())) {
      //load hook and manager
      loadHookAndManager(globalConfig.getPackageHook());
    }
    cacheEntity(globalConfig);
    AssistEntityOperatorImpl.DEFAULT_API_ADDRESS_PREFIX = this.restAddr;
    loadMain(globalConfig.getCodeMain());
  }

  private GlobalConfig loadConfig() {
    JsonNode jsonNode = null;
    this.classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("config/global.json");
    if (inputStream == null) throw new LASException("you must have a global.json config file for your cloud code.");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      StringBuilder globalBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) globalBuilder.append(line);
      jsonNode = ZJsonParser.asJsonNode(globalBuilder.toString());
    } catch (LASException e) {
      throw new LASException("Your global.json config is not match json format.Please check your config. Caused by "+ e.getMessage());
    } catch (IOException e) {
      throw new LASException(e);
    }
    return new GlobalConfig(jsonNode);
  }

  public ZEntityManagerHandler getEntityManagerHandler(String managerName) {
    return entityManagerHandlerMap.get(managerName);
  }

  public LASLoader getLoader() {
    return this.loader;
  }

  private void loadMain(String userMainClassPath) throws Exception {
    @SuppressWarnings("unchecked")
    Class<LASLoaderBase> clazz = (Class<LASLoaderBase>) Class.forName(userMainClassPath);
    loader = clazz.newInstance();
    loader.main(globalConfig);
  }

  private void cacheEntity(GlobalConfig globalConfig) {
    //处理不存在hook的entity
    String entityPackage = globalConfig.getPackageEntity();
    if (entityPackage != null && !entityPackage.trim().equals("")) {
      try {
        List<Class<?>> allEntity = getClassesForPackage(classLoader, entityPackage);
        if (allEntity.size() == 0) logger.warn("Your package-entity is empty.You will can't operate any ZEntityManager interfaces.Please check your global.json config");
        allEntity.removeAll(hookEntities);
        //建立空的entityManager
        for (Class<?> entityClazz : allEntity) {
          ZEntityManager entityManager = new ZEntityManagerImpl(globalConfig.getApplicationID(), globalConfig.getApplicationKey(), null, entityClazz, restAddr);
          ZEntityManagerFactory.putManager(entityClazz, entityManager);
          logger.info("cache entity to factory:" + entityClazz.getSimpleName());
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
      }
    } else {
      logger.warn("Your package-entity is empty.You will can't operate any ZEntityManager interfaces.");
    }
  }

  private void loadHookAndManager(String hookPackage) throws Exception {
    URL packageRoot = Thread.currentThread().getContextClassLoader().getResource(hookPackage.replace(".", "/"));
    if (packageRoot == null) throw new LASException("your package-hook is invalid.Please check your global.json config");
    File[] files = new File(packageRoot.getFile()).listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".class");
      }
    });
    for (File file : files) {
      String name = file.getName();
      if (name.endsWith(".class")) {
        parseClass(hookPackage, name.substring(0, name.length() - 6));
      }
    }
  }

  private void parseClass(String hookPackage, String clazzName) {
    try {
      Class hookClazz = Thread.currentThread().getContextClassLoader().loadClass(hookPackage + "." + clazzName);
      EntityManager entityManagerAnnotation = (EntityManager) hookClazz.getAnnotation(EntityManager.class);
      if (entityManagerAnnotation == null) return;
      String managerName = entityManagerAnnotation.value();
      if (managerName.equals("")) {
        String[] managerNames = managerName.split("ManagerHook");
        if (managerNames.length >= 2) {
          managerName = managerNames[0];
        } else {
          managerNames = managerName.split("Hook");
          if (managerNames.length >= 2) {
            managerName = managerNames[0];
          } else {
            throw new IllegalArgumentException(String.format("class %s should be annotate value.", hookClazz.getSimpleName()));
          }
        }
      }
      ZEntityManagerHookBase hook = (ZEntityManagerHookBase) hookClazz.newInstance();
      Type[] types = ((ParameterizedType) hookClazz.getGenericSuperclass()).getActualTypeArguments();
      Class entityClazz = (Class) types[0];
      ZEntityManager entityManager = new ZEntityManagerImpl(globalConfig.getApplicationID(), globalConfig.getApplicationKey(), hook, entityClazz, restAddr);
      entityManagerHandlerMap.put(managerName, new ZEntityManagerHandler(entityManager, hook, entityClazz));
      ZEntityManagerFactory.putManager(entityClazz, entityManager);
      logger.info("cache entity to factory:" + entityClazz.getSimpleName());
      hookEntities.add(entityClazz);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private boolean isBlank(String str) {
    return (str == null || str.trim().equals(""));
  }

  /**
   * Attempts to list all the classes in the specified package as determined
   * by the context class loader
   *
   * @param packageName the package name to search
   * @return a list of classes that exist within that package
   * @throws ClassNotFoundException if something went wrong
   */
  private List<Class<?>> getClassesForPackage(ClassLoader cld, String packageName) throws ClassNotFoundException {
    final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    try {
      if (cld == null) throw new ClassNotFoundException("Can't get class loader.");
      final Enumeration<URL> resources = cld.getResources(packageName.replace('.', '/'));
      URLConnection connection;
      for (URL url; resources.hasMoreElements() && ((url = resources.nextElement()) != null); ) {
        try {
          connection = url.openConnection();
          if (connection instanceof JarURLConnection) {
            checkJarFile((JarURLConnection) connection, packageName, classes);
          } else if (connection instanceof FileURLConnection) {
            try {
              checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
            } catch (final UnsupportedEncodingException ex) {
              throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Unsupported encoding)", ex);
            }
          } else
            throw new ClassNotFoundException(packageName + " (" + url.getPath() + ") does not appear to be a valid package");
        } catch (final IOException e) {
          throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, e);
        }
      }
    } catch (final NullPointerException ex) {
      throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Null pointer exception)", ex);
    } catch (final IOException ioEx) {
      throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, ioEx);
    }
    return classes;
  }

  /**
   * Private helper method
   *
   * @param directory   The directory to start with
   * @param packageName The package name to search for. Will be needed for getting the
   *                    Class object.
   * @param classes     if a file isn't loaded but still is in the directory
   * @throws ClassNotFoundException
   */
  private void checkDirectory(File directory, String packageName, ArrayList<Class<?>> classes) throws ClassNotFoundException {
    File tmpDirectory;
    if (directory.exists() && directory.isDirectory()) {
      final String[] files = directory.list();

      for (final String file : files) {
        if (file.endsWith(".class")) {
          try {
            classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
          } catch (final NoClassDefFoundError e) {
            // do nothing. this class hasn't been found by the
            // loader, and we don't care.
          }
        } else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
          checkDirectory(tmpDirectory, packageName + "." + file, classes);
        }
      }
    }
  }

  /**
   * Private helper method.
   *
   * @param connection  the connection to the jar
   * @param packageName the package name to search for
   * @param classes     the current ArrayList of all classes. This method will simply
   *                    add new classes.
   * @throws ClassNotFoundException if a file isn't loaded but still is in the jar file
   * @throws IOException            if it can't correctly read from the jar file.
   */
  private void checkJarFile(JarURLConnection connection, String packageName, ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
    final JarFile jarFile = connection.getJarFile();
    final Enumeration<JarEntry> entries = jarFile.entries();
    String name;
    for (JarEntry jarEntry; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null); ) {
      name = jarEntry.getName();
      if (name.contains(".class")) {
        name = name.substring(0, name.length() - 6).replace('/', '.');
        if (name.contains(packageName)) {
          classes.add(classLoader.loadClass(name));
        }
      }
    }
  }
}
