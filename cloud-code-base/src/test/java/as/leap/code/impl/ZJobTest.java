package as.leap.code.impl;

import as.leap.code.Request;
import as.leap.code.Response;
import as.leap.code.LASHandler;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by stream.
 */
public class ZJobTest {

  @Test
  public void invokeJob() {
    DefineJob jobs = new DefineJob();
    String jobName = "myJob";

    jobs.define(jobName, new LASHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        Assert.assertEquals("progress", request.parameter(String.class));
        Response<String> jobResponse = new LASResponse<String>(String.class);
        jobResponse.setResult("done");
        return jobResponse;
      }
    });

    //invoke defined job directly.
    String result = (String) jobs.getHandler(jobName).handle(new LASRequest("progress")).getResult();
    Assert.assertTrue("done".equals(result));
  }


}
