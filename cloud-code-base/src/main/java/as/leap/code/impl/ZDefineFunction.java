package as.leap.code.impl;

import as.leap.code.RequestCategory;
import as.leap.code.ZDefiner;

/**
 * Created by stream.
 */
public class ZDefineFunction extends ZAbstractDefiner implements ZDefiner {

  public ZDefineFunction() {
    category = RequestCategory.FUNCTION;
  }
}
