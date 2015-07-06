package as.leap.code;

import java.util.Set;

/**
 * Created by stream.
 */
public interface Definer {

  /**
   * define a handler with specified parameter
   *
   * @param name
   * @param handler
   */
  void define(String name, LASHandler<? extends Request, ? extends Response> handler);

  /**
   * get ZHandler for invoking.
   * the type of Request and Response has been erased while execute get method,
   * so just return the interface.
   *
   * @param name
   * @return
   */
  LASHandler<Request, Response> getZHandler(String name);

  /**
   * get name of all handler
   *
   * @return
   */
  Set<String> getHandlerNames();

  /**
   * there are three kinds of definer
   *
   * @return
   */
  RequestCategory getCategory();
}
