package as.leap.code.test.framework;

import as.leap.code.*;
import as.leap.code.data.access.DataAccessMethod;
import as.leap.code.impl.JobRunner;
import as.leap.code.impl.LASClassManagerHandler;
import as.leap.code.impl.LASRequest;
import com.fasterxml.jackson.databind.JsonNode;
import as.leap.code.impl.LASResponse;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class TestZCloud {

  private static final Logger logger = LoggerFactory.getLogger(TestZCloud.class);
  private BootstrapZCloud bootstrapZCloud;

  protected TestZCloud() throws Exception{
    this.bootstrapZCloud = new BootstrapZCloud();
    this.bootstrapZCloud.start();
  }

  protected TestZCloud(String restAddr) throws Exception{
    this.bootstrapZCloud = new BootstrapZCloud();
    bootstrapZCloud.setRestAddr(restAddr);
    this.bootstrapZCloud.start();
  }

  protected Response runFunction(String name, String params) {
    Request request = new LASRequest(params);
    Response response = null;
    Definer definer = bootstrapZCloud.getLoader().definers().get(RequestCategory.FUNCTION.alias());
    if (definer == null) {
      System.err.println("doesn't exist function definer");
    } else {
      LASHandler<Request, Response> handler = definer.getZHandler(name);
      if (handler != null) {
        try {
          response = handler.handle(request);
        } catch (Throwable e) {
          e.printStackTrace();
          StackTraceElement[] elements = e.getStackTrace();
          if (elements != null && elements.length > 0) {
            System.err.println(e.toString() + " at " + elements[0]);
          } else {
            System.err.println(e.toString());
          }
        }
      } else {
        response = new LASResponse(String.class);
        response.setError("function " + name + " undefined.");
        System.err.println("function " + name + " undefined.");
      }
    }
    return response;
  }


  protected void runJob(String name, String params) {
    Request request = new LASRequest(params);
    Definer definer = bootstrapZCloud.getLoader().definers().get(RequestCategory.JOB.alias());
    if (definer == null) {
      logger.error("doesn't exist job definer");
    } else {
      LASHandler<Request, Response> handler = definer.getZHandler(name);
      if (handler != null) {
        final JobRunner jobRunner = new JobRunner(handler, request);
        jobRunner.start();
        try {
          jobRunner.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        logger.error("job " + name + " undefined.");
      }
    }
  }

  protected <T> Response runEntityHook(T object, DataAccessMethod method) throws Exception {
    return runEntityHook(object.getClass().getSimpleName(), method, object);
  }

  protected <T> Response runEntityHook(String managerName, DataAccessMethod method, T object) throws Exception {
    LASClassManagerHandler entityManagerHandler = bootstrapZCloud.getEntityManagerHandler(managerName);
    JsonNode params = ZJsonParser.asJsonNode(object);

    Map<String, Object> requestParams = new HashMap<String, Object>();
    requestParams.put("params", params);
    requestParams.put("method", method == DataAccessMethod.FINDBYID ? "findById" : method.name().toLowerCase());

    Request request = new LASRequest(ZJsonParser.asJson(requestParams));
    Response response = entityManagerHandler.handle(request);
    return response;
  }

}
