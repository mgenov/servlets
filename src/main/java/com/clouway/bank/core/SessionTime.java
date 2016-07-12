package com.clouway.bank.core;

/**
 * The implementation of this interface will be used for retrieve current time
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface SessionTime {
  /**
   * Will get current time
   *
   * @return current time
   */
  long getCurrentTime();

  /**
   * Will get time of life of the session
   *
   * @return time
   */
  long getTimeOfLife();
}
