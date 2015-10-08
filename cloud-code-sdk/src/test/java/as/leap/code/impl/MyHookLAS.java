package as.leap.code.impl;

import as.leap.code.AfterResult;
import as.leap.code.BeforeResult;
import as.leap.code.UserPrincipal;
import as.leap.las.sdk.DeleteMsg;
import as.leap.las.sdk.SaveMsg;
import as.leap.las.sdk.UpdateMsg;


/**
 *
 */
public class MyHookLAS extends LASClassManagerHookBase<Song> {

  @Override
  public BeforeResult<Song> beforeCreate(Song entity, UserPrincipal userPrincipal) {
    BeforeResult<Song> beforeResult = new BeforeResult<Song>(entity, true);
    return beforeResult;
  }

  @Override
  public AfterResult afterCreate(BeforeResult<Song> beforeResult, SaveMsg saveMessage, UserPrincipal userPrincipal) {
    return new AfterResult(saveMessage);
  }

  @Override
  public BeforeResult<String> beforeDelete(String objectId, UserPrincipal userPrincipal) {
    return new BeforeResult<String>(objectId, true);
  }

  @Override
  public AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage, UserPrincipal userPrincipal) {
    return new AfterResult(deleteMessage);
  }

  @Override
  public AfterResult afterUpdate(String objectId, UpdateMsg updateMessage, UserPrincipal userPrincipal) {
    return new AfterResult(updateMessage);
  }

  @Override
  public BeforeResult<String[]> beforeDelete(String[] objectIds, UserPrincipal userPrincipal) {
    return new BeforeResult<String[]>(objectIds, true);
  }
}
