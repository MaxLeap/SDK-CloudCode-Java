package com.maxleap.code;


import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.LASQuery;
import com.maxleap.las.sdk.LASUpdate;
import com.maxleap.las.sdk.UpdateMsg;

public interface LASClassManager<T> {

  //default api for use userPrincipal

  SaveResult<T> create(T object, UserPrincipal userPrincipal) throws LASException;

  FindMsg<T> find(LASQuery query, UserPrincipal userPrincipal) throws LASException;

  FindMsg<T> find(LASQuery query, boolean count, UserPrincipal userPrincipal) throws LASException;

  T findById(String id, UserPrincipal userPrincipal) throws LASException;

  UpdateMsg update(String id, LASUpdate update, UserPrincipal userPrincipal) throws LASException;

  DeleteResult delete(String id, UserPrincipal userPrincipal) throws LASException;

  DeleteResult delete(String[] ids, UserPrincipal userPrincipal) throws LASException;

  //api for use masterKey

  SaveResult<T> create(T object) throws LASException;

  FindMsg<T> find(LASQuery query) throws LASException;

  FindMsg<T> find(LASQuery query, boolean count) throws LASException;

  T findById(String id) throws LASException;

  UpdateMsg update(String id, LASUpdate update) throws LASException;

  DeleteResult delete(String id) throws LASException;

  DeleteResult delete(String[] ids) throws LASException;

}
