package as.leap.code;


/**
 * Created by stream.
 */
public interface Request {

  /**
   *
   * @param clazz the class of want to convert
   * @param <T> th class type of want to convert
   * @return object of want to convert
   */
  <T> T parameter(Class<T> clazz);

  boolean isUseMasterKey();

  void useMasterKey();
}
