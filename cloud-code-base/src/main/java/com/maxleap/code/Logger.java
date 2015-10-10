package com.maxleap.code;

/**
 * Created by stream.
 */
public class Logger {

  final LogDelegate logDelegate;

  public Logger(LogDelegate logDelegate) {
    this.logDelegate = logDelegate;
  }

  public void info(String message) {
    this.logDelegate.info(message);
  }

  public void debug(String message) {
    this.logDelegate.debug(message);
  }

  public void warn(Object message) {
    this.logDelegate.warn(message.toString());
  }

  public void warn(final Object message, final Throwable t) {
    this.logDelegate.warn(message.toString(), t);
  }

  public void error(final Object message) {
    this.logDelegate.error(message.toString());
  }

  public void error(final Object message, final Throwable t) {
    this.logDelegate.error(message.toString(), t);
  }


}
