package as.leap.code.impl;

import as.leap.code.Logger;
import as.leap.code.LoggerFactory;
import as.leap.code.assist.classes.ReceiptRegular;

import java.util.Map;

/**
 * Created by shunlv on 15-7-2.
 */
public class ReceiptRegularLASClassManager extends AssistLASClassManagerImpl<ReceiptRegular> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptRegularLASClassManager.class);

  public ReceiptRegularLASClassManager(Class<ReceiptRegular> receiptRegularClass) {
    super(receiptRegularClass);
  }

  public Map transaction(String id, Map receiptInfo) {
    try {
      String response = WebUtils.doPost(getAPIAddress() + "/" + id + "/trans", getHeader(), LASJsonParser.asJson(receiptInfo), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of transaction[" + getAPIAddress() + "]:" + response);
      return LASJsonParser.asMap(response);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }
}
