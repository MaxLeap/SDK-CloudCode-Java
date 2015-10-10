package com.maxleap.code.impl;

import com.maxleap.code.RequestCategory;
import com.maxleap.code.Definer;

/**
 * Created by stream.
 */
public class DefineFunction extends AbstractDefiner implements Definer {

  public DefineFunction() {
    category = RequestCategory.FUNCTION;
  }
}
