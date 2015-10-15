package com.maxleap.code;

/**
 * Created by stream
 */
public class MLException extends RuntimeException {

  public MLException() {
    super();
  }

  public MLException(Throwable cause) {
    super(cause);
  }

  public MLException(String message) {
    super(message);
  }

  public MLException(String message, Throwable cause) {
    super(message, cause);
  }
}
