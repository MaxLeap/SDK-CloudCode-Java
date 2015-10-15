package com.maxleap.code.impl;


import com.maxleap.las.sdk.DeleteMsg;
import com.maxleap.las.sdk.SaveMsg;
import com.maxleap.las.sdk.UpdateMsg;
import com.maxleap.code.AfterResult;
import com.maxleap.code.BeforeResult;
import com.maxleap.code.MLClassManagerHook;
import com.maxleap.code.UserPrincipal;

/**
 * User：poplar
 * Date：14-10-28
 */
public abstract class MLClassManagerHookBase<T> implements MLClassManagerHook<T> {

  @Override
  public BeforeResult<T> beforeCreate(T entity, UserPrincipal userPrincipal) {
    return new BeforeResult<T>(entity, true);
  }

  @Override
  public AfterResult afterCreate(BeforeResult<T> beforeResult, SaveMsg saveMessage, UserPrincipal userPrincipal) {
    AfterResult afterResult = new AfterResult(saveMessage);
    return afterResult;
  }

  @Override
  public BeforeResult<String> beforeDelete(String objectId, UserPrincipal userPrincipal) {
    return new BeforeResult<String>(objectId, true);
  }

  @Override
  public AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage, UserPrincipal userPrincipal) {
    AfterResult afterResult = new AfterResult(deleteMessage);
    return afterResult;
  }

  @Override
  public AfterResult afterUpdate(String objectId, UpdateMsg updateMessage, UserPrincipal userPrincipal) {
    AfterResult afterResult = new AfterResult(updateMessage);
    return afterResult;
  }

  @Override
  public BeforeResult<String[]> beforeDelete(String[] objectIds, UserPrincipal userPrincipal) {
    return new BeforeResult<String[]>(objectIds, true);
  }

}
