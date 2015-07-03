package as.leap.code.impl;

import as.leap.code.Logger;
import as.leap.code.LoggerFactory;
import as.leap.code.assist.entity.Wallet;
import as.leap.las.sdk.UpdateMsg;

import java.util.Map;

/**
 * Created by shunlv on 15-6-8.
 */
public class WalletEntityOperator extends AssistEntityOperatorImpl<Wallet> {
  private static final Logger LOGGER = LoggerFactory.getLogger(WalletEntityOperator.class);

  public WalletEntityOperator(Class<Wallet> walletClass) {
    super(walletClass);
  }

  public UpdateMsg consume(String id, Wallet wallet) {
    try {
      String response = WebUtils.doPost(getAPIAddress() + "/" + id + "/consume", getHeader(), ZJsonParser.asJson(wallet), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of consume[" + getAPIAddress() + "]:" + response);
      return ZJsonParser.asObject(response, UpdateMsg.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  public Map transaction(String id, Map receiptInfo) {
    try {
      String response = WebUtils.doPost(getAPIAddress() + "/" + id + "/trans", getHeader(), ZJsonParser.asJson(receiptInfo), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of transaction[" + getAPIAddress() + "]:" + response);
      return ZJsonParser.asMap(response);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }

  public Wallet getWallet(Map params) {
    try {
      String response = WebUtils.doPost(getAPIAddress() + "/getWallet", getHeader(), ZJsonParser.asJson(params), DEFAULT_TIMEOUT, DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of getWallet[" + getAPIAddress() + "]:" + response);
      return ZJsonParser.asObject(response, Wallet.class);
    } catch (Exception e) {
      throw new as.leap.code.LASException(e);
    }
  }
}
