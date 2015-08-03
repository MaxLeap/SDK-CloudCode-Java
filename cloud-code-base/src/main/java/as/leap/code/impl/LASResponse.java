package as.leap.code.impl;

import as.leap.code.Response;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stream.
 */
public class LASResponse<T> implements Response<T> {
  String logFormat = "Ran cloud %s[%s] with:\n\tInput: %s\n\tResult: %s";

  private static final TypeFactory typeFactory = TypeFactory.defaultInstance();
  private T result;
  private JavaType resultType;
  private String errorMessage;
  private Map<String,String> headers = new HashMap<String, String>();

  public LASResponse(Class<T> resultClass) {
    this(resultClass, false);
  }

  public LASResponse(Class<?> resultClass, boolean isCollection) {
    if (isCollection) {
      resultType = typeFactory.constructCollectionType(List.class, resultClass);
    } else {
      resultType = typeFactory.uncheckedSimpleType(resultClass);
    }
  }

  public JavaType getResultType() {
    return resultType;
  }

  @Override
  public T getResult() {
    return result;
  }

  @Override
  public void setResult(T result) {
    this.result = result;
  }

  @Override
  public void setError(String errorMessage) {
    if (errorMessage != null)
      errorMessage = errorMessage.replaceAll("\\r{1,}|\\n{1,}", "-");
    this.errorMessage = errorMessage;
  }

  @Override
  public String getError() {
    return errorMessage;
  }

  @Override
  public boolean succeeded() {
    return errorMessage == null;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  @Override
  public void putHeader(String key,String value){
    this.headers.put(key,value);
  }
}
