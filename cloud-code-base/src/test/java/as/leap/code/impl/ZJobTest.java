package as.leap.code.impl;

import as.leap.code.Request;
import as.leap.code.Response;
import as.leap.code.ZHandler;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by stream.
 */
public class ZJobTest {

  @Test
  public void invokeJob() {
    ZDefineJob jobs = new ZDefineJob();
    String jobName = "myJob";

    jobs.define(jobName, new ZHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        Assert.assertEquals("progress", request.parameter(String.class));
        Response<String> jobResponse = new ZResponse<String>(String.class);
        jobResponse.setResult("done");
        return jobResponse;
      }
    });

    //invoke defined job directly.
    String result = (String) jobs.getZHandler(jobName).handle(new ZRequest("progress")).getResult();
    Assert.assertTrue("done".equals(result));
  }


}
