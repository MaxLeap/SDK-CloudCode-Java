package as.leap.code.impl;

import as.leap.code.LASException;
import as.leap.code.themis.Themis;
import as.leap.code.themis.classes.CounterEntity;
import as.leap.code.themis.classes.LockEntity;

/**
 * User：poplar
 * Date：15/8/19
 */
public class ThemisImpl implements Themis {

  private String apiAddress;
  final static int DEFAULT_TIMEOUT = 5000;
  final static int DEFAULT_READ_TIMEOUT = 15000;

  public static String DEFAULT_API_ADDRESS_PREFIX = "http://apiuat.zcloud.io/2.0";

  public ThemisImpl() {
    this.apiAddress = DEFAULT_API_ADDRESS_PREFIX;
  }

  @Override
  public CounterEntity generateCounter(CounterEntity counterEntity) {
    try {
      String response = WebUtils.doPost(apiAddress + "/count",null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return LASJsonParser.asObject(response,CounterEntity.class);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long get(CounterEntity counterEntity) {
    String url = apiAddress + "/count/" + counterEntity.getAppId() + "/" + counterEntity.getVersion() + "/" + counterEntity.getName();
    try {
      String response = WebUtils.doGet(url,null,null);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long incrementAndGet(CounterEntity counterEntity) {
    try {
      String response = WebUtils.doPut(apiAddress + "/count/incrementAndGet",null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long getAndIncrement(CounterEntity counterEntity) {
    try {
      String response = WebUtils.doPut(apiAddress + "/count/getAndIncrement",null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long decrementAndGet(CounterEntity counterEntity) {
    try {
      String response = WebUtils.doPut(apiAddress + "/count/decrementAndGet",null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long addAndGet(CounterEntity counterEntity, long value) {
    try {
      String response = WebUtils.doPut(apiAddress + "/count/addAndGet/" + String.valueOf(value),null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long getAndAdd(CounterEntity counterEntity, long value) {
    try {
      String response = WebUtils.doPut(apiAddress + "/count/getAndAdd/" + String.valueOf(value),null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Boolean compareAndSet(CounterEntity counterEntity, long expected, long value) {
    try {
      String response = WebUtils.doPost(apiAddress + "/count/cas/" + String.valueOf(expected) + "/" + String.valueOf(value),null,LASJsonParser.asJson(counterEntity),DEFAULT_TIMEOUT,DEFAULT_READ_TIMEOUT);
      return Boolean.parseBoolean(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public LockEntity getLock(LockEntity lockEntity) {
    String url = apiAddress + "/lock/" + lockEntity.getAppId() + "/" + lockEntity.getVersion() + "/" + lockEntity.getName();
    try {
      String response = WebUtils.doGet(url,null,null);
      return LASJsonParser.asObject(response,LockEntity.class);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public void lockRelease(LockEntity lockEntity) {
    String url = apiAddress + "/lock/" + lockEntity.getAppId() + "/" + lockEntity.getVersion() + "/" + lockEntity.getName();
    try {
      WebUtils.doDelete(url,null,null);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }
}
