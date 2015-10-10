package com.maxleap.code.test.framework;

/**
 *
 */
public interface TestJobMessageHandler {

  void message(Object message);

  void success(Object message);

  void error(String message);

}
