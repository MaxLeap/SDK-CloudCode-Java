package as.leap.code.impl;

import as.leap.code.CloudCodeContants;
import as.leap.code.Logger;
import as.leap.code.LoggerFactory;
import as.leap.code.UserPrincipal;
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

  public Map transaction(String id, Map receiptInfo,UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/" + id + "/trans", CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(receiptInfo), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of transaction[" + apiAddress + "/" + id + "/trans]:" + response);
      return LASJsonParser.asMap(response);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  public Map transaction(String id, Map receiptInfo) {
    return this.transaction(id,receiptInfo,null);
  }
}
