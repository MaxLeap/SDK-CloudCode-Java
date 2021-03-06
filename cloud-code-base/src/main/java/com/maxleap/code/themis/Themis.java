package com.maxleap.code.themis;

/**
 * User：poplar
 * Date：15/8/19
 */
public interface Themis {
  /**
   * 获取一个count
   *
   * @param name counterName
   */
  void generateCounter(String name);

  /**
   * 获取值
   *
   * @param name counterName
   * @return long
   */
  Long get(String name);

  /**
   * 增加并获取值
   *
   * @param name counterName
   * @return long
   */
  Long incrementAndGet(String name);


  /**
   * 先获取到值,然后原子增加
   *
   * @param name counterName
   * @return long
   */
  Long getAndIncrement(String name);


  /**
   * 递减并获取值
   *
   * @param name counterName
   * @return long
   */
  Long decrementAndGet(String name);


  /**
   * 增加指定的值并返回结果
   *
   * @param name  counterName
   * @param value long
   * @return long
   */
  Long addAndGet(String name, long value);


  Long getAndAdd(String name, long value);

  /**
   * CAS 一个值
   *
   * @param name     counterName
   * @param expected 期待的值
   * @param value    新的值
   * @return boolean
   */
  Boolean compareAndSet(String name, long expected, long value);


  //---------------------------------------------------------------------------------

  /**
   * 获取一个Lock
   *
   * @param name lockName
   */
  void getLock(String name);

  /**
   * 释放指定的锁
   *
   * @param name lockName
   */
  void lockRelease(String name);

}
