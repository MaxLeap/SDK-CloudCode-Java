package as.leap.code.impl;

import as.leap.code.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stream.
 */
abstract class ZAbstractDefiner implements ZDefiner {

  protected Map<String, ZHandler<? extends Request, ? extends Response>> handlers;
  //what kind of action.
  protected RequestCategory category;

  protected ZAbstractDefiner() {
    this.handlers = new ConcurrentHashMap<String, ZHandler<? extends Request, ? extends Response>>();
  }

  @Override
  public ZHandler<Request, Response> getZHandler(String name) {
    return (ZHandler<Request, Response>) handlers.get(name);
  }

  @Override
  public RequestCategory getCategory() {
    return category;
  }

  @Override
  public void define(String name, ZHandler<? extends Request, ? extends Response> handler) {
    handlers.put(name, handler);
  }

  @Override
  public Set<String> getHandlerNames() {
    return handlers.keySet();
  }
}
