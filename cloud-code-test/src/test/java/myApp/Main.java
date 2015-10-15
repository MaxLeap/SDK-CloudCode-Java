package myApp;

import com.maxleap.code.*;
import com.maxleap.code.impl.GlobalConfig;
import com.maxleap.code.impl.LoaderBase;
import com.maxleap.code.impl.MLResponse;

/**
 *
 */
public class Main extends LoaderBase implements Loader {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void main(GlobalConfig globalConfig) {
    defineFunction("hello", new MLHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult("hello" + params);
        return response;
      }
    });

    defineJob("myJob", new MLHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new MLResponse<String>(String.class);
        response.setResult(params + " success");
        return response;
      }
    });
  }
}
