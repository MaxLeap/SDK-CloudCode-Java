package as.leap.code;

/**
 * stream.
 */
public interface LogDelegate {

  void info(String message);

  void debug(String message);

  void warn(Object message);

  void warn(final Object message, final Throwable t);

  void error(final Object message);

  void error(final Object message, final Throwable t);

}
