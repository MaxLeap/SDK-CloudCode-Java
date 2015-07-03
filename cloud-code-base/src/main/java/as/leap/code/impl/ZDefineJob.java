package as.leap.code.impl;

import as.leap.code.RequestCategory;
import as.leap.code.ZDefiner;

/**
 * Job have to running in the background.
 * so, we have to deploy a worker or just make a thread.
 * <p>
 * Created by stream.
 */
public class ZDefineJob extends ZAbstractDefiner implements ZDefiner {

  public ZDefineJob() {
    category = RequestCategory.JOB;
  }

}
