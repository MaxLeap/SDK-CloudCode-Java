package as.leap.code;

import java.util.Set;

/**
 * Created by stream.
 */
public interface Definer {

  /**
   * define a handler with specified parameter
   *
   * @param name handler name
   * @param handler function/job handler
   */
  void define(String name, LASHandler<? extends Request, ? extends Response> handler);

  /**
   * get ZHandler for invoking.
   * the type of Request and Response has been erased while execute get method,
   * so just return the interface.
   *
   * @param name handler name
   * @return function/job handler
   */
  LASHandler<Request, Response> getHandler(String name);

  /**
   * get name of all handler
   *
   * @return collection of handler name
   */
  Set<String> getHandlerNames();

  /**
   * there are three kinds of definer
   *
   * @return definer category
   */
  RequestCategory getCategory();
}
