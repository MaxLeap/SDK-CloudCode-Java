package com.maxleap.code;


import com.maxleap.las.sdk.SaveMsg;

/**
 *
 */
public class SaveResult<T> {
  private BeforeResult<T> beforeResult;
  private SaveMsg saveMessage;
  private String failMessage;

  public SaveResult() {
  }

  public SaveResult(String failMessage) {
    this.failMessage = failMessage;
  }

  public SaveResult(BeforeResult<T> beforeResult, SaveMsg saveMessage) {
    this.beforeResult = beforeResult;
    this.saveMessage = saveMessage;
  }

  public void setSaveMessage(SaveMsg saveMessage) {
    this.saveMessage = saveMessage;
  }

  public void setFailMessage(String failMessage) {
    this.failMessage = failMessage;
  }

  public String getFailMessage() {
    return failMessage;
  }

  public boolean isSuccess() {
    return failMessage == null;
  }

  public void setBeforeResult(BeforeResult<T> beforeResult) {
    this.beforeResult = beforeResult;
  }

  public BeforeResult<T> getBeforeResult() {
    return beforeResult;
  }

  public SaveMsg getSaveMessage() {
    return saveMessage;
  }
}
