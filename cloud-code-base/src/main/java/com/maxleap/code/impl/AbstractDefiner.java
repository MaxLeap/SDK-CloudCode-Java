package com.maxleap.code.impl;

import com.maxleap.code.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stream.
 */
abstract class AbstractDefiner implements Definer {

  protected Map<String, MLHandler<? extends Request, ? extends Response>> handlers;
  //what kind of action.
  protected RequestCategory category;

  protected AbstractDefiner() {
    this.handlers = new ConcurrentHashMap<String, MLHandler<? extends Request, ? extends Response>>();
  }

  @Override
  public MLHandler<Request, Response> getHandler(String name) {
    return (MLHandler<Request, Response>) handlers.get(name);
  }

  @Override
  public RequestCategory getCategory() {
    return category;
  }

  @Override
  public void define(String name, MLHandler<? extends Request, ? extends Response> handler) {
    handlers.put(name, handler);
  }

  @Override
  public Set<String> getHandlerNames() {
    return handlers.keySet();
  }
}
