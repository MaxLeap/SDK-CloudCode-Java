package as.leap.code.impl;

import as.leap.code.assist.entity.Wallet;
import as.leap.las.sdk.*;
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
    WalletEntityOperator.DEFAULT_API_ADDRESS_PREFIX = "http://apiuat.zcloud.io/2.0";
  }

  @Test
  public void baseOperatorLifeCycleTest() {
    WalletEntityOperator walletEntityOperator = new WalletEntityOperator(Wallet.class);
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
    System.out.println(ZJsonParser.asJson(wallet1));
    Assert.assertEquals("testuid", wallet1.getuId());

    //update
    LASUpdate update = LASUpdate.getUpdate();
    update.set("uId", "testuid");
    UpdateMsg updateMsg = walletEntityOperator.update(saveMsg.objectIdString(), update);
    Assert.assertNotNull(updateMsg);
    System.out.println(updateMsg);
    Assert.assertTrue(updateMsg.number() == 1);

    //find
    LASQuery lasQuery = LASQuery.instance();
    lasQuery.equalTo("uId", "testuid");
    FindMsg<Wallet> walletFindMsg = walletEntityOperator.find(lasQuery);
    Assert.assertNotNull(walletFindMsg);
    System.out.println(walletFindMsg);
    Assert.assertNotNull(walletFindMsg.results());
    Assert.assertTrue(walletFindMsg.results().size() > 0);
    Assert.assertEquals("testuid", walletFindMsg.results().get(0).getuId());
    Assert.assertNotNull(walletFindMsg.results().get(0).objectIdString());

    //consume
    Wallet wallet2 = new Wallet();
    wallet2.setProducts(new HashMap<String, Double>());
    try {
      UpdateMsg updateMsg1 = walletEntityOperator.consume(saveMsg.objectIdString(), wallet2);
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
