package com.maxleap.code.assist;

import com.maxleap.code.UserPrincipal;
import com.maxleap.las.sdk.*;

/**
 * User：poplar
 * Date：15-6-2
 */
public interface AssistMLClassManager<T> {

  //======api for use userPrincipal======

  SaveMsg create(T coin, UserPrincipal userPrincipal);

  T findById(String id, UserPrincipal userPrincipal);

  UpdateMsg update(String id, MLUpdate update, UserPrincipal userPrincipal);

  DeleteMsg delete(String id, UserPrincipal userPrincipal);

  FindMsg<T> find(MLQuery query, UserPrincipal userPrincipal);


  //=========api for use masterKey=========

  SaveMsg create(T coin);

  T findById(String id);

  UpdateMsg update(String id, MLUpdate update);

  DeleteMsg delete(String id);

  FindMsg<T> find(MLQuery query);
}
