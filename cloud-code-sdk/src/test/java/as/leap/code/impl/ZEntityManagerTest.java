package as.leap.code.impl;

import as.leap.code.DeleteResult;
import as.leap.code.SaveResult;
import as.leap.code.ZEntityManagerHook;
import as.leap.las.sdk.FindMsg;
import as.leap.las.sdk.LASQuery;
import as.leap.las.sdk.LASUpdate;
import as.leap.las.sdk.UpdateMsg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by stream.
 */
public class ZEntityManagerTest {

  private ZEntityManagerImpl<Song> entityManager;
  private String appId = "53d21c66e4b04663ccc7fbfd";
  private String masterKey = "U9IcZuSwUyVWTgCjEA";
  private ZEntityManagerHook<Song> hook;

  @Before
  public void setup() {
    hook = new MyHook();
    entityManager = new ZEntityManagerImpl<Song>(appId, masterKey, hook, Song.class);
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
