package myApp.service;

import com.maxleap.code.*;
import com.maxleap.code.impl.MLResponse;
import myApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User：David Young
 * Date：15/12/30
 */
@Service
public class UserService {

  @Autowired
  private MLClassManager<User> userManager;

  public MLHandler hello(){
    return new MLHandler() {
      @Override
      public Response handle(Request request) {
        System.out.println("###"+userManager);
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult("hello" + params);
        return response;
      }
    };
  }

  public MLHandler helloJob(){
    return new MLHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult(params + " success");
        return response;
      }
    };
  }

}
