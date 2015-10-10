package com.maxleap.code.impl;


import com.maxleap.code.LogDelegate;
import com.maxleap.code.LogDelegateFactory;

/**
 * Created by stream.
 */
public class JULLoggerDelegateFactory implements LogDelegateFactory {

  @Override
  public LogDelegate createDelegate(String name) {
    return new JULLogger(java.util.logging.Logger.getLogger(name));
  }
}
