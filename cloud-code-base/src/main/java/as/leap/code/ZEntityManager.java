package as.leap.code;


import as.leap.las.sdk.FindMsg;
import as.leap.las.sdk.LASQuery;
import as.leap.las.sdk.LASUpdate;
import as.leap.las.sdk.UpdateMsg;

public interface ZEntityManager<T> {

  SaveResult<T> create(T object) throws LASException;

  FindMsg<T> find(LASQuery query) throws LASException;

  FindMsg<T> find(LASQuery query, boolean count) throws LASException;

  T findById(String id) throws LASException;

  UpdateMsg update(String id, LASUpdate update) throws LASException;

  DeleteResult delete(String id) throws LASException;

  DeleteResult delete(String[] ids) throws LASException;

}
