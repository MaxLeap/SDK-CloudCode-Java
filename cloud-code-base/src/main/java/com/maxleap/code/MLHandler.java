package com.maxleap.code;

/**
 * CloudCode Handler.
 * Created by stream.
 */
public interface MLHandler<T extends Request, R extends Response> {
  R handle(T request);
}
