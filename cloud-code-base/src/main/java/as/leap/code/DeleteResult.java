package as.leap.code;


import as.leap.las.sdk.DeleteMsg;

/**
 *
 */
public class DeleteResult<T> {
  private BeforeResult<T> beforeResult;
  private DeleteMsg deleteMessage;
  private String failMessage;

  public DeleteResult(String failMessage) {
    this.failMessage = failMessage;
  }

  public DeleteResult(BeforeResult<T> beforeResult) {
    this.beforeResult = beforeResult;
  }

  public DeleteResult(BeforeResult<T> beforeResult, DeleteMsg deleteMessage) {
    this.beforeResult = beforeResult;
    this.deleteMessage = deleteMessage;
  }

  public String getFailMessage() {
    return failMessage;
  }

  public void setFailMessage(String failMessage) {
    this.failMessage = failMessage;
  }

  public boolean isSuccess() {
    return failMessage == null;
  }

  public BeforeResult<T> getBeforeResult() {
    return beforeResult;
  }

  public void setBeforeResult(BeforeResult<T> beforeResult) {
    this.beforeResult = beforeResult;
  }

  public DeleteMsg getDeleteMessage() {
    return deleteMessage;
  }

  public void setDeleteMessage(DeleteMsg deleteMessage) {
    this.deleteMessage = deleteMessage;
  }
}
