package myApp;

import as.leap.code.*;
import as.leap.code.impl.GlobalConfig;
import as.leap.code.impl.LASLoaderBase;
import as.leap.code.impl.ZResponse;

/**
 *
 */
public class Main extends LASLoaderBase implements LASLoader {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void main(GlobalConfig globalConfig) {
    defineFunction("hello", new ZHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new ZResponse<String>(String.class);
        response.setResult("hello" + params);
        return response;
      }
    });

    defineJob("myJob", new ZHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new ZResponse<String>(String.class);
        response.setResult(params + " success");
        return response;
      }
    });
  }
}
