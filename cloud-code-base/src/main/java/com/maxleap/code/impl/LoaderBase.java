package com.maxleap.code.impl;

import com.maxleap.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stream.
 */
public abstract class LoaderBase implements Loader {
  private Logger logger = LoggerFactory.getLogger(LoaderBase.class);
  private Map<String, Definer> definers;

  private Definer functionDefiner = new DefineFunction();
  private Definer jobDefiner = new DefineJob();
  private Definer managerDefiner = new DefineMLClassManager();

  protected LoaderBase() {
    this.definers = new HashMap<String, Definer>();
    definers.put(functionDefiner.getCategory().alias(), functionDefiner);
    definers.put(jobDefiner.getCategory().alias(), jobDefiner);
    definers.put(managerDefiner.getCategory().alias(), managerDefiner);
  }

  @Override
  public Map<String, Definer> definers() {
    return definers;
  }

  protected void defineFunction(String name, MLHandler<? extends Request, ? extends Response> handler) {
    functionDefiner.define(name, handler);
    logger.info("Deployed Cloud Function :" + name);
  }

  protected void defineJob(String name, MLHandler<? extends Request, ? extends Response> handler) {
    jobDefiner.define(name, handler);
    logger.info("Deployed Cloud Job :" + name);
  }

  void defineClassesManager(String name, MLHandler<? extends Request, ? extends Response> handler) {
    managerDefiner.define(name, handler);
    logger.info("Deployed Cloud Classes Manager :" + name);
  }
}
