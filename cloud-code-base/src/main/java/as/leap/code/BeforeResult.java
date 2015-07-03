package as.leap.code;

/**
 * stream
 */
public class BeforeResult<T> {

  /*the parameter of request*/
  private T origin;

  /*checking result*/
  private boolean result;

  /*the reason of fail, for the response*/
  private String failMessage;

  public BeforeResult() {
  }

  public BeforeResult(T origin) {
    this.origin = origin;
  }

  public BeforeResult(T origin, boolean result) {
    this.origin = origin;
    this.result = result;
  }

  public BeforeResult(T origin, boolean result, String failMessage) {
    this.origin = origin;
    this.result = result;
    this.failMessage = failMessage;
  }

  public T getOrigin() {
    return origin;
  }

  public void setOrigin(T origin) {
    this.origin = origin;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getFailMessage() {
    return failMessage;
  }

  public void setFailMessage(String failMessage) {
    this.failMessage = failMessage;
  }

  @Override
  public String toString() {
    return "BeforeResult{" +
        "origin=" + origin +
        ", result=" + result +
        ", failMessage='" + failMessage + '\'' +
        '}';
  }
}
