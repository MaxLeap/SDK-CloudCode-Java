package com.maxleap.code;


import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.MLQuery;
import com.maxleap.las.sdk.MLUpdate;
import com.maxleap.las.sdk.UpdateMsg;

public interface MLClassManager<T> {

  //default api for use userPrincipal

  SaveResult<T> create(T object, UserPrincipal userPrincipal) throws MLException;

  FindMsg<T> find(MLQuery query, UserPrincipal userPrincipal) throws MLException;

  FindMsg<T> find(MLQuery query, boolean count, UserPrincipal userPrincipal) throws MLException;

  T findById(String id, UserPrincipal userPrincipal) throws MLException;

  UpdateMsg update(String id, MLUpdate update, UserPrincipal userPrincipal) throws MLException;

  DeleteResult delete(String id, UserPrincipal userPrincipal) throws MLException;

  DeleteResult delete(String[] ids, UserPrincipal userPrincipal) throws MLException;

  //api for use masterKey

  SaveResult<T> create(T object) throws MLException;

  FindMsg<T> find(MLQuery query) throws MLException;

  FindMsg<T> find(MLQuery query, boolean count) throws MLException;

  T findById(String id) throws MLException;

  UpdateMsg update(String id, MLUpdate update) throws MLException;

  DeleteResult delete(String id) throws MLException;

  DeleteResult delete(String[] ids) throws MLException;

}
