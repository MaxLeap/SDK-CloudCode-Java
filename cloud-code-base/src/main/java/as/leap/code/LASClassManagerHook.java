package as.leap.code;


import as.leap.las.sdk.DeleteMsg;
import as.leap.las.sdk.SaveMsg;
import as.leap.las.sdk.UpdateMsg;

/**
 * The hooks for some db operation.
 */
public interface LASClassManagerHook<T> {

  /**
   * hook before save
   *
   * @param entity operator object
   * @return BeforeResult result of before create entity
   */
  BeforeResult<T> beforeCreate(T entity);

  /**
   * hook after save
   *
   * @param beforeResult result of before create entity
   * @param saveMessage result of create entity
   * @return AfterResult result of after create entity
   */
  AfterResult afterCreate(BeforeResult<T> beforeResult, SaveMsg saveMessage);

  /**
   * hook before delete
   *
   * @param objectId entity object id
   * @return BeforeResult result of before delete entity
   */
  BeforeResult<String> beforeDelete(String objectId);

  /**
   * hook before delete
   *
   * @param objectIds collection of entity objects id
   * @return BeforeResult result of before delete some of entity object by id
   */
  BeforeResult<String[]> beforeDelete(String[] objectIds);

  /**
   * hook after delete
   * @param beforeResult result of before delete entity
   * @param deleteMessage result of delete entity
   * @return result of after delete entity
   */
  AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage);

  /**
   * do something after completed update operation.
   * @param objectId object id of entity
   * @param updateMessage result of update entity
   * @return result of after delete entity
   */
  AfterResult afterUpdate(String objectId, UpdateMsg updateMessage);


}
