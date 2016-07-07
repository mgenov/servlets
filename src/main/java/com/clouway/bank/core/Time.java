package com.clouway.bank.core;

/**
 * The implementation of this interface will be used for retrieve current time
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Time {
  /**
   * Will get current time
   *
   * @return current time
   */
  long getCurrentTime();

  /**
   * Will set time of life
   *
   * @return time
   */
  long setTimeOfLife();
}
