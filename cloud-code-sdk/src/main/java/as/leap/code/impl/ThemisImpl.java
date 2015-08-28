package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import as.leap.code.LASException;
import as.leap.code.Logger;
import as.leap.code.LoggerFactory;
import as.leap.code.themis.Themis;

import java.util.HashMap;
import java.util.Map;

/**
 * User：poplar
 * Date：15/8/19
 */
public class ThemisImpl implements Themis {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThemisImpl.class);

  private String version;
  private String countAddress;
  private String lockAddress;
  private Map<String,String> headers = new HashMap<String, String>();

  public ThemisImpl() {
    this.version = CloudCodeContants.GLOBAL_CONFIG.getVersion();
    this.countAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + "/themis/count/";
    this.lockAddress = CloudCodeContants.DEFAULT_API_ADDRESS_PREFIX + "/themis/lock/";
    headers.put(CloudCodeContants.HEADER_ZCLOUD_APPID, CloudCodeContants.GLOBAL_CONFIG.getApplicationID());
    headers.put(CloudCodeContants.HEADER_ZCLOUD_MASTERKEY, CloudCodeContants.GLOBAL_CONFIG.getApplicationKey());
  }

  private String getFullCountAddress(String operator,String name){
    if (operator == null)
      return countAddress + version + "/" + name;
    return countAddress + operator + "/" + version + "/" + name;
  }

  private String getFullLockAddress(String name){
    return lockAddress + version + "/" + name;
  }

  @Override
  public void generateCounter(String name) {
    try {
      String response = WebUtils.doPost(getFullCountAddress(null,name),headers,null, CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of generateCounter[" + getFullCountAddress(null,name) + "]:" + response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long get(String name) {
    try {
      String response = WebUtils.doGet(getFullCountAddress(null,name),headers,null);
      LOGGER.info("get response of get[" + getFullCountAddress(null,name) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long incrementAndGet(String name) {
    try {
      String response = WebUtils.doPut(getFullCountAddress("incrementAndGet",name),headers,"",CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of incrementAndGet[" + getFullCountAddress("incrementAndGet",name) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long getAndIncrement(String name) {
    try {
      String response = WebUtils.doPut(getFullCountAddress("getAndIncrement",name),headers,"",CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of getAndIncrement[" + getFullCountAddress("getAndIncrement",name) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long decrementAndGet(String name) {
    try {
      String response = WebUtils.doPut(getFullCountAddress("decrementAndGet",name),headers,"",CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of decrementAndGet[" + getFullCountAddress("decrementAndGet",name) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long addAndGet(String name, long value) {
    try {
      String response = WebUtils.doPut(getFullCountAddress("addAndGet",name) + "/" + String.valueOf(value),headers,"",CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of addAndGet[" + getFullCountAddress("addAndGet",name) + "/" + String.valueOf(value) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Long getAndAdd(String name, long value) {
    try {
      String response = WebUtils.doPut(getFullCountAddress("getAndAdd",name) + "/" + String.valueOf(value),headers,"",CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of getAndAdd[" + getFullCountAddress("getAndAdd",name) + "/" + String.valueOf(value) + "]:" + response);
      return Long.parseLong(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public Boolean compareAndSet(String name, long expected, long value) {
    try {
      String response = WebUtils.doPost(getFullCountAddress("getAndAdd",name) + "/" + String.valueOf(expected) + "/" + String.valueOf(value),headers,null,CloudCodeContants.DEFAULT_TIMEOUT,CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of compareAndSet[" + getFullCountAddress("compareAndSet",name) + "/" + String.valueOf(expected) + "/" + String.valueOf(value) + "]:" + response);
      return Boolean.parseBoolean(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public void getLock(String name) {
    try {
      String response = WebUtils.doGet(getFullLockAddress(name),headers,null);
      LOGGER.info("get response of getLock[" + getFullLockAddress(name) + "]:" + response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  @Override
  public void lockRelease(String name) {
    try {
      String response = WebUtils.doDelete(getFullLockAddress(name),headers,null);
      LOGGER.info("get response of getLock[" + getFullLockAddress(name) + "]:" + response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }
}
