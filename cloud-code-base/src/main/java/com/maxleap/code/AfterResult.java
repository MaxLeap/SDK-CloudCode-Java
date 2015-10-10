package com.maxleap.code;

/**
 * stream
 */
public class AfterResult<T> {

  private T result;

  /*checking result*/
  private boolean success;

  /*the reason of fail, for the response*/
  private String failMessage;

  public AfterResult(T result) {
    this.result = result;
    this.success = true;
  }

  public AfterResult(String failMessage) {
    this.success = false;
    this.failMessage = failMessage;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getFailMessage() {
    return failMessage;
  }

  public void setFailMessage(String failMessage) {
    this.failMessage = failMessage;
  }
}
