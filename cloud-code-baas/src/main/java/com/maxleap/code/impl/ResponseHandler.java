package com.maxleap.code.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.maxleap.code.Logger;
import com.maxleap.code.LoggerFactory;
import io.vertx.core.http.HttpServerResponse;

import java.util.Map;


/**
 *
 */
class ResponseHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class);

  void handle(MLResponse response, HttpServerResponse httpResponse) {
    try {
      Map<String, String> headers = response.getHeaders();
      if (headers.size() > 0) {
        for (Map.Entry<String,String> entry : headers.entrySet()) {
          httpResponse.putHeader(entry.getKey(),entry.getValue());
        }
      }
      if (response.succeeded()) {
        JavaType type = response.getResultType();
        String result;
        if (type.isPrimitive() ||
            type.getRawClass() == Integer.class ||
            type.getRawClass() == String.class ||
            type.getRawClass() == Long.class ||
            type.getRawClass() == Boolean.class ||
            type.getRawClass() == Float.class ||
            type.getRawClass() == Double.class) {
          result = response.getResult().toString();
        } else {
          result = MLJsonParser.asJson(response.getResult());
        }
        httpResponse.end(result);
      } else {
        httpResponse.setStatusCode(545);
        httpResponse.setStatusMessage(response.getError());
        httpResponse.end();
      }
    } catch (Exception e) {
      httpResponse.setStatusCode(545);
      httpResponse.setStatusMessage(e.getMessage());
      httpResponse.end();
    }
  }

}
