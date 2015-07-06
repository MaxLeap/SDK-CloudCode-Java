package as.leap.code.assist;

import as.leap.las.sdk.*;

/**
 * User：poplar
 * Date：15-6-2
 */
public interface AssistLASClassManager<T> {
  SaveMsg create(T coin);

  T findById(String id);

  UpdateMsg update(String id, LASUpdate update);

  DeleteMsg delete(String id);

  FindMsg<T> find(LASQuery query);
}
