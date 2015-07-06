package myApp;

import as.leap.code.*;
import as.leap.code.impl.GlobalConfig;
import as.leap.code.impl.LoaderBase;
import as.leap.code.impl.LASResponse;

/**
 *
 */
public class Main extends LoaderBase implements Loader {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void main(GlobalConfig globalConfig) {
    defineFunction("hello", new LASHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new LASResponse<String>(String.class);
        response.setResult("hello" + params);
        return response;
      }
    });

    defineJob("myJob", new LASHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        String params = request.parameter(String.class);
        Response<String> response = new LASResponse<String>(String.class);
        response.setResult(params + " success");
        return response;
      }
    });
  }
}
