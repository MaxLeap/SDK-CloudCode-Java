package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import as.leap.code.LASException;
import as.leap.code.Logger;
import as.leap.code.LoggerFactory;
import as.leap.code.assist.Path;
import as.leap.code.assist.classes.PushMsgBuilder;

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
    if (this.criteria == null) throw new LASException("your criteria must not be empty");
    if (this.data == null) throw new LASException("your message must not be empty");
    if (this.message == null) super.build();
    try {
      String response = WebUtils.doPost(apiAddress, CloudCodeContants.HEADERS, message, CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of push[" + apiAddress + "]:" + response);
    } catch (IOException e) {
      throw new LASException(e);
    }
  }

}
