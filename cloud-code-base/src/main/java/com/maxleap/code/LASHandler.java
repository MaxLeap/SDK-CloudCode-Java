package com.maxleap.code;

/**
 * CloudCode Handler.
 * Created by stream.
 */
public interface LASHandler<T extends Request, R extends Response> {
  R handle(T request);
}
