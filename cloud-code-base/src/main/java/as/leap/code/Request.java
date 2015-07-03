package as.leap.code;


/**
 * Created by stream.
 */
public interface Request {

  /**
   * @return T
   */
  <T> T parameter(Class<T> clazz);

  boolean isUseMasterKey();

  void useMasterKey();
}
