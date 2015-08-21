package as.leap.code.themis;

import as.leap.code.themis.classes.CounterEntity;
import as.leap.code.themis.classes.LockEntity;

/**
 * User：poplar
 * Date：15/8/19
 */
public interface Themis {
  /**
   * 获取一个count
   *
   * @param counterEntity counterName
   * @return Count
   */
  CounterEntity generateCounter(CounterEntity counterEntity);

  /**
   * 获取值
   *
   * @return long
   */
  Long get(CounterEntity counterEntity);

  /**
   * 增加并获取值
   *
   * @return long
   */
  Long incrementAndGet(CounterEntity counterEntity);


  /**
   * 先获取到值,然后原子增加
   *
   * @return long
   */
  Long getAndIncrement(CounterEntity counterEntity);


  /**
   * 递减并获取值
   *
   * @return long
   */
  Long decrementAndGet(CounterEntity counterEntity);


  /**
   * 增加指定的值并返回结果
   *
   * @param value long
   * @return long
   */
  Long addAndGet(CounterEntity counterEntity, long value);


  Long getAndAdd(CounterEntity counterEntity, long value);

  /**
   * CAS 一个值
   *
   * @param expected 期待的值
   * @param value    新的值
   * @return boolean
   */
  Boolean compareAndSet(CounterEntity counterEntity, long expected, long value);


  //---------------------------------------------------------------------------------

  /**
   * 获取一个Lock
   *
   * @param lockEntity lock
   * @return Lock
   */
  LockEntity getLock(LockEntity lockEntity);

  /**
   * 释放指定的锁
   *
   * @param lockEntity lock
   * @return void
   */
  void lockRelease(LockEntity lockEntity);

}
