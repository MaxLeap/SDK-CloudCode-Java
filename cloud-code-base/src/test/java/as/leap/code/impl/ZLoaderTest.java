package as.leap.code.impl;

import as.leap.code.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by stream.
 */
public class ZLoaderTest {

  private static class MyLoader extends LoaderBase implements Loader {
    @Override
    public void main(GlobalConfig config) {
      defineFunction("hello", new LASHandler<LASRequest, LASResponse<String>>() {
        @Override
        public LASResponse<String> handle(LASRequest request) {
          Assert.assertNotNull(request);
          return new LASResponse<String>(String.class);
        }
      });
    }
  }

  @Test
  public void loader() {
    Loader loader = new MyLoader();
    loader.main(null);
    Assert.assertEquals(3, loader.definers().size());
    LASHandler<Request, Response> handler = loader.definers().get(RequestCategory.FUNCTION.alias()).getHandler("hello");
    Assert.assertNotNull(handler.handle(new LASRequest(null)));
  }

}
