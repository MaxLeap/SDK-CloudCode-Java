package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import org.junit.Before;
import org.junit.Test;

/**
 * User：poplar
 * Date：15/8/27
 */
public class PushMsgTest {

  @Before
  public void before(){
    CloudCodeContants.init();
    CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = "http://10.10.10.193:8080";
  }

  @Test
  public void push(){
    PushMsg msg = new PushMsg();
    msg.withDeviceToken("adbsdfsfd").build().push();
  }
}
