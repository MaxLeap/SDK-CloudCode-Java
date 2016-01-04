package com.maxleap.code.test.framework;

import com.maxleap.code.Response;
import com.maxleap.code.data.access.DataAccessMethod;
import myApp.entity.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class FrameworkTest extends TestCloudCode{

//  public FrameworkTest() throws Exception {
//    super("http://10.10.10.193:8080");
//  }

  @Test
  public void function() {
    Response response = runFunction("hello", " world");
    Assert.assertNotNull(response);
    Assert.assertTrue(response.succeeded());
    Assert.assertEquals("hello world", response.getResult());
  }

  @Test
  public void job() {
    runJob("helloJob", "{\"value\":\"test\"}");
  }

  @Ignore
  @Test
  public void hook() {
    try {
      User user = new User();
      user.setId(1);
      user.setName("name");

      Response response = runEntityHook(user, DataAccessMethod.CREATE);
      Assert.assertTrue(response.succeeded());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
