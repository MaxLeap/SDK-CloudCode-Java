package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import as.leap.code.themis.Themis;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User：poplar
 * Date：15/8/19
 */
public class ThemisTest {
  private Themis themis;
  private String counterEntity = "myCounter";
  private String lockEntity = "myLock";

  @Before
  public void before(){
    CloudCodeContants.init();
    CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX = "http://10.10.10.193:8080";
    themis = new ThemisImpl();
  }

  @Test
  public void count(){
    themis.generateCounter(counterEntity);
    Long count = themis.get(counterEntity);
    System.out.println("get="+count);
    Long count1 = themis.incrementAndGet(counterEntity);
    System.out.println("incrementAndGet="+count1);
    Assert.assertEquals(count + 1, count1.longValue());
    Long count2 = themis.get(counterEntity);
    System.out.println("get="+count2);
    Assert.assertEquals(count + 1, count2.longValue());
    Long count3 = themis.getAndIncrement(counterEntity);
    System.out.println("getAndIncrement=" + count3);
    Assert.assertEquals(count + 1, count3.longValue());
    Long count4 = themis.get(counterEntity);
    System.out.println("get=" + count4);
    Assert.assertEquals(count + 2, count4.longValue());
    Long count5 = themis.decrementAndGet(counterEntity);
    System.out.println("decrementAndGet="+count5);
    Assert.assertEquals(count + 1, count5.longValue());
    Long count6 = themis.get(counterEntity);
    System.out.println("get=" + count6);
    Assert.assertEquals(count + 1, count6.longValue());
    Long count7 = themis.addAndGet(counterEntity, 1);
    System.out.println("addAndGet="+count7);
    Assert.assertEquals(count + 2, count7.longValue());
    Long count8 = themis.get(counterEntity);
    System.out.println("get=" + count8);
    Assert.assertEquals(count + 2, count8.longValue());
    Long count9 = themis.getAndAdd(counterEntity, 1);
    System.out.println("getAndAdd="+count9);
    Assert.assertEquals(count + 2, count9.longValue());
    Long count10 = themis.get(counterEntity);
    System.out.println("get=" + count10);
    Assert.assertEquals(count + 3, count10.longValue());
  }

  @Test
  public void lock(){
    themis.getLock(lockEntity);
    themis.lockRelease(lockEntity);
  }
}
