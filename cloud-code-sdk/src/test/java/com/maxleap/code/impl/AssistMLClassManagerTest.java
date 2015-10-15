package com.maxleap.code.impl;

import com.maxleap.code.CloudCodeContants;
import com.maxleap.code.assist.AssistMLClassManager;
import com.maxleap.code.assist.classes.Coin;
import com.maxleap.las.sdk.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User：poplar
 * Date：15-6-3
 */
public class AssistMLClassManagerTest {

  @Before
  public void before() {
    CloudCodeContants.init();
  }

  @Test
  public void baseOperatorLifeCycleTest() {
    AssistMLClassManager<Coin> coinService = new AssistMLClassManagerImpl<Coin>(Coin.class);
    //create
    Coin coin = new Coin();
    coin.setDesc("mytestDesc");
    coin.setName("mytest");
    SaveMsg saveMsg = coinService.create(coin);
    Assert.assertNotNull(saveMsg);
    Assert.assertNotNull(saveMsg.objectId());
    Assert.assertTrue(saveMsg.createdAt() > 0);
    System.out.println(saveMsg);

    //findById
    Coin coin2 = coinService.findById(saveMsg.objectIdString());
    Assert.assertNotNull(coin2);
    System.out.println(MLJsonParser.asJson(coin2));
    Assert.assertEquals("mytest", coin2.getName());
    Assert.assertEquals("mytestDesc", coin2.getDesc());

    //update
    MLUpdate update = MLUpdate.getUpdate();
    update.set("name", "mytest_new");
    update.set("desc", "mytestDesc_new");
    UpdateMsg updateMsg = coinService.update(saveMsg.objectIdString(), update);
    Assert.assertNotNull(updateMsg);
    System.out.println(updateMsg);
    Assert.assertTrue(updateMsg.number() == 1);

    //find
    MLQuery lasQuery = MLQuery.instance();
    lasQuery.equalTo("name", "mytest_new");
    FindMsg<Coin> coinFindMsg = coinService.find(lasQuery);
    Assert.assertNotNull(coinFindMsg);
    System.out.println(coinFindMsg);
    Assert.assertNotNull(coinFindMsg.results());
    Assert.assertTrue(coinFindMsg.results().size() > 0);
    Assert.assertEquals("mytest_new", coinFindMsg.results().get(0).getName());
    Assert.assertEquals("mytestDesc_new", coinFindMsg.results().get(0).getDesc());
    Assert.assertNotNull(coinFindMsg.results().get(0).objectIdString());

    //delete
    DeleteMsg deleteMsg = coinService.delete(coinFindMsg.results().get(0).objectIdString());
    Assert.assertNotNull(deleteMsg);
    Assert.assertTrue(deleteMsg.number() == 1);
  }
}
