package com.maxleap.code.impl;

import com.maxleap.code.LogDelegate;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by stream.
 */
public class JULLogger implements LogDelegate {

  private java.util.logging.Logger logger;

  public JULLogger(java.util.logging.Logger logger) {
    this.logger = logger;
  }

  public void info(String message) {
    log(Level.INFO, message);
  }

  @Override
  public void debug(String message) {
    log(Level.FINEST, message);
  }

  public void warn(Object message) {
    log(Level.WARNING, message);
  }

  public void warn(final Object message, final Throwable t) {
    log(Level.WARNING, message, t);
  }

  public void error(final Object message) {
    log(Level.SEVERE, message);
  }

  public void error(final Object message, final Throwable t) {
    log(Level.SEVERE, message, t);
  }

  private void log(Level level, Object message) {
    log(level, message, null);
  }

  private void log(Level level, Object message, Throwable t) {
    if (!logger.isLoggable(level)) {
      return;
    }
    String msg = (message == null) ? "NULL" : message.toString();
    LogRecord record = new LogRecord(level, msg);
    record.setLoggerName(logger.getName());
    record.setThrown(t);
    logger.log(record);
  }
}
