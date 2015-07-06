package as.leap.code.impl;

import as.leap.code.RequestCategory;
import as.leap.code.Definer;

/**
 * Job have to running in the background.
 * so, we have to deploy a worker or just make a thread.
 * <p>
 * Created by stream.
 */
public class DefineJob extends AbstractDefiner implements Definer {

  public DefineJob() {
    category = RequestCategory.JOB;
  }

}
