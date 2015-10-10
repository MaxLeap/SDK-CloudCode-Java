package com.maxleap.code;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class LoggerFactory {

  private static volatile LogDelegateFactory delegateFactory;
  private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();

  static {
    initialise();
  }

  private static synchronized void initialise() {
    LogDelegateFactory delegateFactory;
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Class<?> clz;
    try {
      clz = loader.loadClass("com.maxleap.code.impl.Log4j2DelegateFactory");
    } catch (ClassNotFoundException e) {
      try {
        clz = loader.loadClass("com.maxleap.code.impl.JULLoggerDelegateFactory");
      } catch (ClassNotFoundException e1) {
        throw new IllegalArgumentException("can not load Log class", e1);
      }
    }
    try {
      delegateFactory = (LogDelegateFactory) clz.newInstance();
    } catch (Exception e) {
      throw new IllegalArgumentException("Error instantiating transformer Log class", e);
    }
    LoggerFactory.delegateFactory = delegateFactory;
  }

  public static Logger getLogger(final Class<?> clazz) {
    return getLogger(clazz.getCanonicalName());
  }

  public static Logger getLogger(final String name) {
    Logger logger = loggers.get(name);
    if (logger == null) {
      LogDelegate delegate = delegateFactory.createDelegate(name);
      logger = new Logger(delegate);
      Logger oldLogger = loggers.putIfAbsent(name, logger);
      if (oldLogger != null) {
        logger = oldLogger;
      }
    }
    return logger;
  }
}
