package com.maxleap.code.impl;

import com.maxleap.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stream.
 */
public final class Loader {
  private static Logger logger = LoggerFactory.getLogger(Loader.class);
  private static Map<String, Definer> definers;

  private static Definer functionDefiner = new DefineFunction();
  private static Definer jobDefiner = new DefineJob();
  private static Definer managerDefiner = new DefineMLClassManager();

  static {
    definers = new HashMap<String, Definer>();
    definers.put(functionDefiner.getCategory().alias(), functionDefiner);
    definers.put(jobDefiner.getCategory().alias(), jobDefiner);
    definers.put(managerDefiner.getCategory().alias(), managerDefiner);
  }

  public static Map<String, Definer> definers() {
    return definers;
  }

  public static void defineFunction(String name, MLHandler<? extends Request, ? extends Response> handler) {
    functionDefiner.define(name, handler);
    logger.info("Deployed Cloud Function :" + name);
  }

  public static void defineJob(String name, MLHandler<? extends Request, ? extends Response> handler) {
    jobDefiner.define(name, handler);
    logger.info("Deployed Cloud Job :" + name);
  }

  public static void defineClassesManager(String name, MLHandler<? extends Request, ? extends Response> handler) {
    managerDefiner.define(name, handler);
    logger.info("Deployed Cloud Classes Manager :" + name);
  }
}
