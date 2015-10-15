package com.maxleap.code.impl;

import com.maxleap.code.CloudCodeContants;
import com.maxleap.code.assist.classes.Wallet;
import com.maxleap.las.sdk.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shunlv on 15-6-15.
 */
public class WalletEntityOperatorTest {

  @Before
  public void before() {
    CloudCodeContants.init();
  }

  @Test
  public void baseOperatorLifeCycleTest() {
    WalletMLClassManager walletEntityOperator = new WalletMLClassManager(Wallet.class);
    //create
    Wallet wallet = new Wallet();
    wallet.setuId("testuid");
    SaveMsg saveMsg = walletEntityOperator.create(wallet);
    Assert.assertNotNull(saveMsg);
    Assert.assertNotNull(saveMsg.objectId());
    Assert.assertTrue(saveMsg.createdAt() > 0);
    System.out.println(saveMsg);

    //findById
    Wallet wallet1 = walletEntityOperator.findById(saveMsg.objectIdString());
    Assert.assertNotNull(wallet1);
    System.out.println(MLJsonParser.asJson(wallet1));
    Assert.assertEquals("testuid", wallet1.getuId());

    //update
    MLUpdate update = MLUpdate.getUpdate();
    update.set("uId", "testuid");
    UpdateMsg updateMsg = walletEntityOperator.update(saveMsg.objectIdString(), update);
    Assert.assertNotNull(updateMsg);
    System.out.println(updateMsg);
    Assert.assertTrue(updateMsg.number() == 1);

    //find
    MLQuery lasQuery = MLQuery.instance();
    lasQuery.equalTo("uId", "testuid");
    FindMsg<Wallet> walletFindMsg = walletEntityOperator.find(lasQuery);
    Assert.assertNotNull(walletFindMsg);
    System.out.println(walletFindMsg);
    Assert.assertNotNull(walletFindMsg.results());
    Assert.assertTrue(walletFindMsg.results().size() > 0);
    Assert.assertEquals("testuid", walletFindMsg.results().get(0).getuId());
    Assert.assertNotNull(walletFindMsg.results().get(0).objectIdString());

    //consume
    Map params1 = new HashMap();
    params1.put("products", new HashMap());
    params1.put("walletId", saveMsg.objectIdString());
    try {
      UpdateMsg updateMsg1 = walletEntityOperator.consume(params1);
    } catch (Exception e) {
      Assert.assertTrue(e.getMessage().contains("products can't be null!"));
    }

    //transaction
    try {
      Map result = walletEntityOperator.transaction(saveMsg.objectIdString(), new HashMap());
    } catch (Exception e) {
      Assert.assertTrue(e.getMessage().contains("receiptRegularId can't be null!"));
    }

    // getWallet
    Map params = new HashMap();
    params.put("uId", "testuid");
    Wallet wallet3 = walletEntityOperator.getWallet(params);
    Assert.assertNotNull(wallet3);
    Assert.assertEquals(saveMsg.objectIdString(), wallet3.objectIdString());

    //delete
    DeleteMsg deleteMsg = walletEntityOperator.delete(walletFindMsg.results().get(0).objectIdString());
    Assert.assertNotNull(deleteMsg);
    Assert.assertTrue(deleteMsg.number() == 1);
  }
}
