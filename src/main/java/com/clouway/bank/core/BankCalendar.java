package com.clouway.bank.core;

/**
 * Calendar for the bank
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface BankCalendar {

  /**
   * Gets time for a given period into the future
   *
   * @return Long for a given period
   */
  Long getExpirationTime();


  /**
   * Current time
   *
   * @return Long of the current time
   */
  Long getCurrentTime();
}
