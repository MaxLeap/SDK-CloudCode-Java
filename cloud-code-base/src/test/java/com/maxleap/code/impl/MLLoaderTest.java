package com.maxleap.code.impl;

import com.maxleap.code.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Created by stream.
 */
public class MLLoaderTest {

  private static class MyLoader extends LoaderBase implements Loader {
    @Override
    public void main(ApplicationContext context,GlobalConfig config) {
      defineFunction("hello", new MLHandler<MLRequest, MLResponse<String>>() {
        @Override
        public MLResponse<String> handle(MLRequest request) {
          Assert.assertNotNull(request);
          return new MLResponse<String>(String.class);
        }
      });
    }
  }

  @Test
  public void loader() {
    Loader loader = new MyLoader();
    loader.main(null,null);
    Assert.assertEquals(3, loader.definers().size());
    MLHandler<Request, Response> handler = loader.definers().get(RequestCategory.FUNCTION.alias()).getHandler("hello");
    Assert.assertNotNull(handler.handle(new MLRequest(null, null)));
  }

}
