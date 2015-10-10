package com.maxleap.code;


import com.maxleap.las.sdk.DeleteMsg;
import com.maxleap.las.sdk.SaveMsg;
import com.maxleap.las.sdk.UpdateMsg;

/**
 * The hooks for some db operation.
 */
public interface LASClassManagerHook<T> {

  /**
   * hook before save
   *
   * @param entity operator object
   * @param userPrincipal userPrincipal
   * @return BeforeResult result of before create entity
   */
  BeforeResult<T> beforeCreate(T entity, UserPrincipal userPrincipal);

  /**
   * hook after save
   *
   * @param beforeResult result of before create entity
   * @param saveMessage  result of create entity
   * @param userPrincipal userPrincipal
   * @return AfterResult result of after create entity
   */
  AfterResult afterCreate(BeforeResult<T> beforeResult, SaveMsg saveMessage, UserPrincipal userPrincipal);

  /**
   * hook before delete
   *
   * @param objectId entity object id
   * @param userPrincipal userPrincipal
   * @return BeforeResult result of before delete entity
   */
  BeforeResult<String> beforeDelete(String objectId, UserPrincipal userPrincipal);

  /**
   * hook before delete
   *
   * @param objectIds collection of entity objects id
   * @param userPrincipal userPrincipal
   * @return BeforeResult result of before delete some of entity object by id
   */
  BeforeResult<String[]> beforeDelete(String[] objectIds, UserPrincipal userPrincipal);

  /**
   * hook after delete
   *
   * @param beforeResult  result of before delete entity
   * @param deleteMessage result of delete entity
   * @param userPrincipal userPrincipal
   * @return result of after delete entity
   */
  AfterResult afterDelete(BeforeResult<String> beforeResult, DeleteMsg deleteMessage, UserPrincipal userPrincipal);

  /**
   * do something after completed update operation.
   *
   * @param objectId      object id of entity
   * @param updateMessage result of update entity
   * @param userPrincipal userPrincipal
   * @return result of after delete entity
   */
  AfterResult afterUpdate(String objectId, UpdateMsg updateMessage, UserPrincipal userPrincipal);


}
