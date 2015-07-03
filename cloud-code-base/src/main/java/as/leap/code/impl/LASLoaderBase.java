package as.leap.code.impl;

import as.leap.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stream.
 */
public abstract class LASLoaderBase implements LASLoader {
  private Logger logger = LoggerFactory.getLogger(LASLoaderBase.class);
  private Map<String, ZDefiner> definers;

  private ZDefiner functionDefiner = new ZDefineFunction();
  private ZDefiner jobDefiner = new ZDefineJob();
  private ZDefiner managerDefiner = new ZDefineEntityManager();

  protected LASLoaderBase() {
    this.definers = new HashMap<String, ZDefiner>();
    definers.put(functionDefiner.getCategory().alias(), functionDefiner);
    definers.put(jobDefiner.getCategory().alias(), jobDefiner);
    definers.put(managerDefiner.getCategory().alias(), managerDefiner);
  }

  @Override
  public Map<String, ZDefiner> definers() {
    return definers;
  }

  protected void defineFunction(String name, ZHandler<? extends Request, ? extends Response> handler) {
    functionDefiner.define(name, handler);
    logger.info("Deployed Cloud Function :" + name);
  }

  protected void defineJob(String name, ZHandler<? extends Request, ? extends Response> handler) {
    jobDefiner.define(name, handler);
    logger.info("Deployed Cloud Job :" + name);
  }

  void defineManager(String name, ZHandler<? extends Request, ? extends Response> handler) {
    managerDefiner.define(name, handler);
    logger.info("Deployed Cloud Entity Manager :" + name);
  }
}
