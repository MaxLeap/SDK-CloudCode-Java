package com.maxleap.code;

/**
 * Created by stream
 */
public class LASException extends RuntimeException {

  public LASException() {
    super();
  }

  public LASException(Throwable cause) {
    super(cause);
  }

  public LASException(String message) {
    super(message);
  }

  public LASException(String message, Throwable cause) {
    super(message, cause);
  }
}
