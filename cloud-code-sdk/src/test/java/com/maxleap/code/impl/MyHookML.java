package com.maxleap.code.impl;

import com.maxleap.code.AfterResult;
import com.maxleap.code.BeforeResult;
import com.maxleap.code.UserPrincipal;
import com.maxleap.las.sdk.DeleteMsg;
import com.maxleap.las.sdk.SaveMsg;
import com.maxleap.las.sdk.UpdateMsg;


/**
 *
 */
public class MyHookML extends MLClassManagerHookBase<Song> {

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
