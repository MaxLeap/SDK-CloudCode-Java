package as.leap.code.impl;


import as.leap.code.LogDelegate;
import as.leap.code.LogDelegateFactory;

/**
 * Created by stream.
 */
public class JULLoggerDelegateFactory implements LogDelegateFactory {

  @Override
  public LogDelegate createDelegate(String name) {
    return new JULLogger(java.util.logging.Logger.getLogger(name));
  }
}
