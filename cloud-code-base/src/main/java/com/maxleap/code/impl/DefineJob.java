package com.maxleap.code.impl;

import com.maxleap.code.RequestCategory;
import com.maxleap.code.Definer;

/**
 * Job have to running in the background.
 * so, we have to deploy a worker or just make a thread.
 *
 * Created by stream.
 */
public class DefineJob extends AbstractDefiner implements Definer {

  public DefineJob() {
    category = RequestCategory.JOB;
  }

}
