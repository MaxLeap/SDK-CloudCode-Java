package myApp.service;

import com.maxleap.code.MLClassManager;
import com.maxleap.code.MLHandler;
import com.maxleap.code.Request;
import com.maxleap.code.Response;
import com.maxleap.code.impl.MLResponse;
import com.maxleap.code.spring.Function;
import myApp.entity.User;

import javax.annotation.Resource;

/**
 * User：David Young
 * Date：15/12/30
 */
@Function("hello")
public class UserService2 implements MLHandler {

  @Resource
  private MLClassManager<User> userManager;

  @Override
  public Response handle(Request request) {
    System.out.println("###"+userManager);
    String params = request.parameter(String.class);
    Response<String> response = new MLResponse<String>(String.class);
    response.setResult("hello" + params);
    return response;
  }
}
