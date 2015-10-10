package com.maxleap.code.impl;

import com.maxleap.code.CloudCodeContants;
import com.maxleap.code.DeleteResult;
import com.maxleap.code.SaveResult;
import com.maxleap.code.LASClassManagerHook;
import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.LASQuery;
import com.maxleap.las.sdk.LASUpdate;
import com.maxleap.las.sdk.UpdateMsg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by stream.
 */
public class LASClassManagerTest {

  private LASClassManagerImpl<Song> entityManager;
  private LASClassManagerHook<Song> hook;

  @Before
  public void setup() {
    CloudCodeContants.init();
    hook = new MyHookLAS();
    entityManager = new LASClassManagerImpl<Song>(hook, Song.class);
  }

  @Test
  public void lifeCycle() {
    final StringBuffer songId = new StringBuffer();
    final Song song = new Song();
    song.setTitle("title" + UUID.randomUUID());
    song.setPlayTimes(10);
    SaveResult<Song> saveResult = entityManager.create(song);
    Assert.assertTrue(saveResult.getBeforeResult().isResult());
    Assert.assertNotNull(saveResult.getSaveMessage().objectId());
    Assert.assertTrue(saveResult.getSaveMessage().createdAt() > 0);
    songId.append(saveResult.getSaveMessage().objectId().toString());

    //
    Song findResult = entityManager.findById(songId.toString());
    Assert.assertNotNull(findResult);
    Assert.assertEquals(song.getTitle(), findResult.getTitle());
    Assert.assertEquals(song.getPlayTimes(), findResult.getPlayTimes());

    //
    LASUpdate update = new LASUpdate();
    update.set("title", song.getTitle() + "_new");
    update.inc("playTimes", 1);
    UpdateMsg updateMsg = entityManager.update(songId.toString(), update);
    Assert.assertNotNull(updateMsg.number());
    Assert.assertTrue(updateMsg.number() == 1);
    Assert.assertTrue(updateMsg.updatedAt() > 0);

    //
    findResult = entityManager.findById(songId.toString());
    Assert.assertNotNull(findResult);
    Assert.assertEquals(song.getTitle() + "_new", findResult.getTitle());
    Assert.assertTrue((song.getPlayTimes() + 1) == findResult.getPlayTimes());

    //
    LASQuery sunQuery = new LASQuery();
    sunQuery.equalTo("objectId", songId.toString());
    FindMsg<Song> songs = entityManager.find(sunQuery, true);
    Assert.assertNotNull(songs);
    Assert.assertTrue(songs.results().size() > 0);

    //
    DeleteResult deleteResult = entityManager.delete(songId.toString());
    Assert.assertTrue(deleteResult.getBeforeResult().isResult());
    Assert.assertNotNull(deleteResult.getDeleteMessage().number());
    Assert.assertTrue(deleteResult.getDeleteMessage().number() == 1);
  }

}
