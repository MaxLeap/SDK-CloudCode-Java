package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User：poplar
 * Date：15/8/27
 */
public class PushMsgTest {

  @Before
  public void before(){
    CloudCodeContants.init();
    CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = "http://10.10.10.193:8080";
  }

  @Test
  public void push(){
    PushMsg msg = new PushMsg();
    msg.withDeviceToken("adbsdfsfd").withMsg("hello").push();
  }

  @Test
  public void test() throws IOException, InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(100);
    final CountDownLatch countDownLatch = new CountDownLatch(100);
    final AtomicInteger count = new AtomicInteger();
    for (int i = 0; i < 100;i++) {
      service.submit(new Runnable() {
        @Override
        public void run() {
          try {
//            String result = WebUtils.doGet("http://10.10.10.193:8080/functions/hello", CloudCodeContants.getHeaders(null), null);
            String result = WebUtils.doGet("http://api.maxleap.cn/2.0/functions/hello", CloudCodeContants.getHeaders(null), null);
//            String result = WebUtils.doGet("http://10.10.10.193:8080/console/functionNames", CloudCodeContants.getHeaders(null), null);
            System.out.println(count.incrementAndGet() + result);
          } catch (IOException e) {
            e.printStackTrace();
          }
          countDownLatch.countDown();
        }
      });
    }
    service.shutdown();
    countDownLatch.await();
//    service.awaitTermination(1, TimeUnit.MINUTES);
  }
}
