package as.leap.code.impl;

import as.leap.code.AfterResult;
import as.leap.code.BeforeResult;
import as.leap.las.sdk.DeleteMsg;
import as.leap.las.sdk.SaveMsg;
import as.leap.las.sdk.UpdateMsg;


/**
 *
 */
public class MyHook extends ZEntityManagerHookBase<Song> {

  @Override
  public BeforeResult<Song> beforeCreate(Song entity) {
    BeforeResult<Song> beforeResult = new BeforeResult<Song>(entity, true);
    return beforeResult;
  }

  @Override
  public AfterResult afterCreate(BeforeResult<Song> beforeResult, SaveMsg saveMessage) {
    return new AfterResult(saveMessage);
  }

  @Override
  public BeforeResult<String> beforeDelete(String objectId) {
    return new BeforeResult<String>(objectId, true);
  }

  @Override
  public AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage) {
    return new AfterResult(deleteMessage);
  }

  @Override
  public AfterResult afterUpdate(String objectId, UpdateMsg updateMessage) {
    return new AfterResult(updateMessage);
  }

  @Override
  public BeforeResult<String[]> beforeDelete(String[] objectIds) {
    return new BeforeResult<String[]>(objectIds, true);
  }
}
