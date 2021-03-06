package com.maxleap.code.impl;

import com.maxleap.code.*;
import com.maxleap.code.UserPrincipal;
import com.maxleap.code.assist.classes.ReceiptRegular;
import com.maxleap.code.Logger;
import com.maxleap.code.LoggerFactory;

import java.util.Map;

/**
 * Created by shunlv on 15-7-2.
 */
public class ReceiptRegularMLClassManager extends AssistMLClassManagerImpl<ReceiptRegular> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptRegularMLClassManager.class);

  public ReceiptRegularMLClassManager(Class<ReceiptRegular> receiptRegularClass) {
    super(receiptRegularClass);
  }

  public Map transaction(String id, Map receiptInfo, UserPrincipal userPrincipal) {
    try {
      String response = WebUtils.doPost(apiAddress + "/" + id + "/trans", CloudCodeContants.getHeaders(userPrincipal), MLJsonParser.asJson(receiptInfo), CloudCodeContants.DEFAULT_TIMEOUT, CloudCodeContants.DEFAULT_READ_TIMEOUT);
      LOGGER.info("get response of transaction[" + apiAddress + "/" + id + "/trans]:" + response);
      return MLJsonParser.asMap(response);
    } catch (Exception e) {
      throw new MLException(e);
    }
  }

  public Map transaction(String id, Map receiptInfo) {
    return this.transaction(id, receiptInfo, null);
  }
}
