package com.maxleap.code.impl;

import com.maxleap.code.CloudCodeContants;
import com.maxleap.code.MLException;
import com.maxleap.code.Logger;
import com.maxleap.code.LoggerFactory;
import com.maxleap.code.assist.Path;
import com.maxleap.code.assist.classes.PushMsgBuilder;

import java.io.IOException;

/**
 * User：poplar
 * Date：15/8/25
 */
public class PushMsg extends PushMsgBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(PushMsg.class);

  private String apiAddress;

  public PushMsg() {
    String path = PushMsgBuilder.class.getAnnotation(Path.class).value();
    this.apiAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + path;
  }

  @Override
  public void push() {
    if (this.criteria == null) throw new MLException("your criteria must not be empty");
    if (this.data == null) throw new MLException("your message must not be empty");
    if (this.message == null) super.build();
    try {
      String response = WebUtils.doPost(apiAddress, CloudCodeContants.getHeaders(null), message, CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of push[" + apiAddress + "]("+message+"):" + response);
    } catch (IOException e) {
      throw new MLException(e);
    }
  }

}
