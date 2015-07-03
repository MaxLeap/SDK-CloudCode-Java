package as.leap.code.impl;


import as.leap.code.*;
import as.leap.las.sdk.DeleteMsg;
import as.leap.las.sdk.SaveMsg;
import as.leap.las.sdk.UpdateMsg;

/**
 * User：poplar
 * Date：14-10-28
 */
public abstract class ZEntityManagerHookBase<T> implements ZEntityManagerHook<T> {

  @Override
  public BeforeResult<T> beforeCreate(T entity) {
    return new BeforeResult<T>(entity, true);
  }

  @Override
  public AfterResult afterCreate(BeforeResult<T> beforeResult, SaveMsg saveMessage) {
    AfterResult afterResult = new AfterResult(saveMessage);
    return afterResult;
  }

  @Override
  public BeforeResult<String> beforeDelete(String objectId) {
    return new BeforeResult<String>(objectId, true);
  }

  @Override
  public AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage) {
    AfterResult afterResult = new AfterResult(deleteMessage);
    return afterResult;
  }

  @Override
  public AfterResult afterUpdate(String objectId, UpdateMsg updateMessage) {
    AfterResult afterResult = new AfterResult(updateMessage);
    return afterResult;
  }

  @Override
  public BeforeResult<String[]> beforeDelete(String[] objectIds) {
    return new BeforeResult<String[]>(objectIds, true);
  }

}
