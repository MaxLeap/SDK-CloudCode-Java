package as.leap.code;


/**
 * Created by stream.
 */
public interface Response<T> {

  T getResult();

  void setResult(T result);

  void setError(String errorMessage);

  String getError();

  boolean succeeded();

  void putHeader(String name,String value);

}
