package as.leap.code;


import as.leap.las.sdk.DeleteMsg;
import as.leap.las.sdk.SaveMsg;
import as.leap.las.sdk.UpdateMsg;

/**
 * The hooks for some db operation.
 */
public interface ZEntityManagerHook<T> {

  /**
   * hook before save
   *
   * @param entity
   * @return BeforeResult
   */
  BeforeResult<T> beforeCreate(T entity);

  /**
   * hook after save
   *
   * @param beforeResult hook result
   */
  AfterResult afterCreate(BeforeResult<T> beforeResult, SaveMsg saveMessage);

  /**
   * hook before delete
   *
   * @param objectId
   * @return BeforeResult
   */
  BeforeResult<String> beforeDelete(String objectId);

  /**
   * hook before delete
   *
   * @param objectIds
   * @return BeforeResult
   */
  BeforeResult<String[]> beforeDelete(String[] objectIds);

  /**
   * @param beforeResult
   */
  AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage);

  /**
   * do something after completed update operation.
   *
   * @param objectId
   */
  AfterResult afterUpdate(String objectId, UpdateMsg updateMessage);


}
