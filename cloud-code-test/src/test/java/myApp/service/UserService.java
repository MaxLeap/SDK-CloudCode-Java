package myApp.service;

import com.maxleap.code.*;
import com.maxleap.code.impl.GlobalConfig;
import com.maxleap.code.impl.MLResponse;
import com.maxleap.code.spring.Function;
import com.maxleap.code.spring.Job;
import myApp.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User：David Young
 * Date：15/12/30
 */
@Service
public class UserService {

  @Resource
  private MLClassManager<User> userManager;

  @Resource
  private GlobalConfig globalConfig;

  @Function("hello")
  public MLHandler hello(){
    return new MLHandler() {
      @Override
      public Response handle(Request request) {
        System.out.println("###"+userManager);
        System.out.println(globalConfig.getConfigJsonStr());
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult("hello" + params);
        return response;
      }
    };
  }

  @Job("helloJob")
  public MLHandler helloJob(){
    return new MLHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        System.out.println("###"+userManager);
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult(params + " success");
        return response;
      }
    };
  }

}
