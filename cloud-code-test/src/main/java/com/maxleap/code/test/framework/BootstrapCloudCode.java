package com.maxleap.code.test.framework;

import com.maxleap.code.*;
import com.maxleap.code.impl.*;
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
class BootstrapCloudCode {

  private GlobalConfig globalConfig;
  private static final Logger logger = LoggerFactory.getLogger(BootstrapCloudCode.class);
  private LoaderBase loader;
  private ClassLoader classLoader;
  private Set<Class> hookClasses;
  private Map<String, MLClassManagerHandler> classesManagerHandlerMap = new ConcurrentHashMap<String, MLClassManagerHandler>();

  public BootstrapCloudCode() {
    CloudCodeContants.init();
    globalConfig = CloudCodeContants.GLOBAL_CONFIG;
    hookClasses = new HashSet<Class>();
    this.classLoader = Thread.currentThread().getContextClassLoader();
    adapterRegion();
  }

  protected void setRestAddr(String restAddr) {
    CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = restAddr;
  }

  protected void start() {
    //load hook and manager
    loadHookAndManager();
    cacheClasses();
    loadMain(globalConfig.getCodeMain());
  }

  private enum Region {
    US("http://api.maxleap.com/2.0"), CN("http://api.maxleap.cn/2.0");

    private String checkAppIdAddress;

    Region(String checkAppIdAddress) {
      this.checkAppIdAddress = checkAppIdAddress;
    }

    public String getCheckAppIdAddress() {
      return this.checkAppIdAddress + "/apps/%s/info";
    }
  }

  private void adapterRegion() {
    if (checkAppId(Region.CN)) {
      CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = Region.CN.checkAppIdAddress;
    } else if (checkAppId(Region.US)) {
      CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = Region.US.checkAppIdAddress;
    } else {
      logger.warn("Does not exist app with id "+CloudCodeContants.GLOBAL_CONFIG.getApplicationID()+" from region[" + Region.CN.name() + "," + Region.US.name() + "]");
    }
  }

  private boolean checkAppId(Region region) {
    String result = null;
    try {
      result = WebUtils.doGet(String.format(region.getCheckAppIdAddress(), CloudCodeContants.GLOBAL_CONFIG.getApplicationID()), CloudCodeContants.getHeaders(null), null);
    } catch (IOException e) {
      if (!e.getMessage().contains("Does not exist app with id " + CloudCodeContants.GLOBAL_CONFIG.getApplicationID()))
        logger.warn("check appId from region[" + region.name() + "] fail:" + e.getMessage());
      return Boolean.FALSE;
    }
    Map<String, Object> map = MLJsonParser.asMap(result);
    if (map != null && map.containsKey("objectId") && map.get("objectId").equals(CloudCodeContants.GLOBAL_CONFIG.getApplicationID()))
      return Boolean.TRUE;
    else return false;
  }

  public MLClassManagerHandler getClassesManagerHandler(String managerName) {
    return classesManagerHandlerMap.get(managerName);
  }

  public Loader getLoader() {
    return this.loader;
  }

  private void loadMain(String userMainClassPath) {
    try {
      @SuppressWarnings("unchecked")
      Class<LoaderBase> clazz = (Class<LoaderBase>) Class.forName(userMainClassPath);
      loader = clazz.newInstance();
      loader.main(globalConfig);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  private void cacheClasses() {
    //处理不存在hook的entity
    String classesPackage = globalConfig.getPackageClasses();
    if (isBlank(classesPackage)) {
      logger.warn("Your packageClasses is empty.You will can't operate any MLClassesManager interfaces.");
      return;
    }
    try {
      List<Class<?>> allClasses = getClassesForPackage(classLoader, classesPackage);
      if (allClasses.size() == 0) {
        logger.warn("Your packageClasses is empty.You will can't operate any MLClassesManager interfaces.Please check your global.json config");
        return;
      }
      allClasses.removeAll(hookClasses);
      //建立空的entityManager
      for (Class<?> classes : allClasses) {
        MLClassManager classesManager = new MLClassManagerImpl(null, classes);
        MLClassManagerFactory.putManager(classes, classesManager);
        logger.info("cache entity to factory:" + classes.getSimpleName());
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }

  private void loadHookAndManager() {
    String hookPackage = globalConfig.getPackageHook();
    if (isBlank(hookPackage)) return;

    URL packageRoot = classLoader.getResource(hookPackage.replace(".", "/"));
    if (packageRoot == null) throw new MLException("your packageHook is invalid.Please check your global.json config");
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
      Class hookClazz = classLoader.loadClass(hookPackage + "." + clazzName);
      ClassManager classManagerAnnotation = (ClassManager) hookClazz.getAnnotation(ClassManager.class);
      if (classManagerAnnotation == null) return;
      String managerName = classManagerAnnotation.value();
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
      MLClassManagerHookBase hook = (MLClassManagerHookBase) hookClazz.newInstance();
      Type[] types = ((ParameterizedType) hookClazz.getGenericSuperclass()).getActualTypeArguments();
      Class classesClazz = (Class) types[0];
      MLClassManager classesManager = new MLClassManagerImpl(hook, classesClazz);
      classesManagerHandlerMap.put(managerName, new MLClassManagerHandler(classesManager, classesClazz));
      MLClassManagerFactory.putManager(classesClazz, classesManager);
      logger.info("cache hook classes to factory:" + classesClazz.getSimpleName());
      hookClasses.add(classesClazz);
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
