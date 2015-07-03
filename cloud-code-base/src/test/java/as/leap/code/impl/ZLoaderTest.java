package as.leap.code.impl;

import as.leap.code.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by stream.
 */
public class ZLoaderTest {

  private static class MyLASLoader extends LASLoaderBase implements LASLoader {
    @Override
    public void main(GlobalConfig config) {
      defineFunction("hello", new ZHandler<ZRequest, ZResponse<String>>() {
        @Override
        public ZResponse<String> handle(ZRequest request) {
          Assert.assertNotNull(request);
          return new ZResponse<String>(String.class);
        }
      });
    }
  }

  @Test
  public void loader() {
    LASLoader loader = new MyLASLoader();
    loader.main(null);
    Assert.assertEquals(3, loader.definers().size());
    ZHandler<Request, Response> handler = loader.definers().get(RequestCategory.FUNCTION.alias()).getZHandler("hello");
    Assert.assertNotNull(handler.handle(new ZRequest(null)));
  }

}
