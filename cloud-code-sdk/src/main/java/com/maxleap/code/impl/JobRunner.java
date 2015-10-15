package com.maxleap.code.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.maxleap.code.*;


/**
 * Created by stream.
 */
public class JobRunner<T> extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);
  private MLHandler<Request, Response<T>> handler;
  private Request request;
  private boolean running;

  public JobRunner(MLHandler<Request, Response<T>> handler, Request request) {
    this.handler = handler;
    this.request = request;
    this.running = true;
  }

  public boolean isRunning() {
    return running;
  }

  @Override
  public void run() {
    try {
      MLResponse<T> response = (MLResponse<T>) handler.handle(request);
      if (response.succeeded()) {
        JavaType type = response.getResultType();
        String result;
        if (type.isPrimitive() ||
            type.getRawClass() == Integer.class ||
            type.getRawClass() == String.class ||
            type.getRawClass() == Long.class ||
            type.getRawClass() == Boolean.class ||
            type.getRawClass() == Float.class) {
          result = response.getResult().toString();
        } else {
          result = MLJsonParser.asJson(response.getResult());
        }
        logger.info(result);
      } else {
        logger.error(response.getError());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    running = false;
  }
}
