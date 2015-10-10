package com.maxleap.code.impl;

import com.maxleap.code.CloudCodeContants;
import com.maxleap.code.Logger;
import com.maxleap.code.LASException;
import com.maxleap.code.LoggerFactory;
import com.maxleap.code.UserPrincipal;
import com.maxleap.code.assist.classes.Wallet;
import com.maxleap.las.sdk.UpdateMsg;

import java.util.Map;

/**
 * Created by shunlv on 15-6-8.
 */
public class WalletLASClassManager extends AssistLASClassManagerImpl<Wallet> {
  private static final Logger LOGGER = LoggerFactory.getLogger(WalletLASClassManager.class);

  public WalletLASClassManager(Class<Wallet> walletClass) {
    super(walletClass);
  }

  public UpdateMsg consume(Map params, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/consume", CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(params), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of consume[" + apiAddress + "/consume]:" + response);
      return LASJsonParser.asObject(response, UpdateMsg.class);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  public Map transaction(String id, Map receiptInfo, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/" + id + "/trans", CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(receiptInfo), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of transaction[" + apiAddress + "/" + id + "/trans]:" + response);
      return LASJsonParser.asMap(response);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  public Wallet getWallet(Map params, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/getWallet", CloudCodeContants.getHeaders(userPrincipal), LASJsonParser.asJson(params), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of getWallet[" + apiAddress + "/getWallet]:" + response);
      return LASJsonParser.asObject(response, Wallet.class);
    } catch (Exception e) {
      throw new LASException(e);
    }
  }

  public UpdateMsg consume(Map params) {
    return this.consume(params, null);
  }

  public Map transaction(String id, Map receiptInfo) {
    return this.transaction(id, receiptInfo, null);
  }

  public Wallet getWallet(Map params) {
    return this.getWallet(params, null);
  }
}
