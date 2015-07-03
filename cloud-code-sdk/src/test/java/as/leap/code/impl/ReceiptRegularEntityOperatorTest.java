package as.leap.code.impl;

import as.leap.code.assist.entity.ReceiptRegular;
import as.leap.las.sdk.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shunlv on 15-7-2.
 */
public class ReceiptRegularEntityOperatorTest {

  @Before
  public void before() {
    ReceiptRegularEntityOperator.DEFAULT_API_ADDRESS_PREFIX = "http://apiuat.zcloud.io/2.0";
  }

  @Test
  public void baseOperatorLifeCycleTest() {
    ReceiptRegularEntityOperator receiptRegularEntityOperator = new ReceiptRegularEntityOperator(ReceiptRegular.class);
    //create
    ReceiptRegular receiptRegular = new ReceiptRegular();
    receiptRegular.setName("slvtest");
    receiptRegular.setStoreProdId("com.toprankapps.instalikes3.handofcoins");
    receiptRegular.setCoinId("testCoinId1");
    receiptRegular.setPlatform("GooglePlay");
    receiptRegular.setAmount(6.6);
    receiptRegular.setCount(100);
    SaveMsg saveMsg = receiptRegularEntityOperator.create(receiptRegular);
    Assert.assertNotNull(saveMsg);
    Assert.assertNotNull(saveMsg.objectId());
    Assert.assertTrue(saveMsg.createdAt() > 0);
    System.out.println(saveMsg);

    //findById
    ReceiptRegular receiptRegular1 = receiptRegularEntityOperator.findById(saveMsg.objectIdString());
    Assert.assertNotNull(receiptRegular1);
    System.out.println(ZJsonParser.asJson(receiptRegular1));
    Assert.assertEquals("slvtest", receiptRegular1.getName());

    //update
    LASUpdate update = LASUpdate.getUpdate();
    update.set("name", "slvtest2");
    UpdateMsg updateMsg = receiptRegularEntityOperator.update(saveMsg.objectIdString(), update);
    Assert.assertNotNull(updateMsg);
    System.out.println(updateMsg);
    Assert.assertTrue(updateMsg.number() == 1);

    //find
    LASQuery lasQuery = LASQuery.instance();
    lasQuery.equalTo("name", "slvtest2");
    FindMsg<ReceiptRegular> receiptRegularFindMsg = receiptRegularEntityOperator.find(lasQuery);
    Assert.assertNotNull(receiptRegularFindMsg);
    System.out.println(receiptRegularFindMsg);
    Assert.assertNotNull(receiptRegularFindMsg.results());
    Assert.assertTrue(receiptRegularFindMsg.results().size() > 0);
    Assert.assertEquals("slvtest2", receiptRegularFindMsg.results().get(0).getName());
    Assert.assertNotNull(receiptRegularFindMsg.results().get(0).objectIdString());

    //transaction
    try {
      Map result = receiptRegularEntityOperator.transaction(saveMsg.objectIdString(), new HashMap());
    } catch (Exception e) {
      Assert.assertTrue(e.getMessage().contains("receipt can't be null!"));
    }

    //delete
    DeleteMsg deleteMsg = receiptRegularEntityOperator.delete(receiptRegularFindMsg.results().get(0).objectIdString());
    Assert.assertNotNull(deleteMsg);
    Assert.assertTrue(deleteMsg.number() == 1);
  }
}
