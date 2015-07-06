package as.leap.code.impl;

import as.leap.code.RequestCategory;
import as.leap.code.Definer;

/**
 * Created by stream.
 */
public class DefineFunction extends AbstractDefiner implements Definer {

  public DefineFunction() {
    category = RequestCategory.FUNCTION;
  }
}
