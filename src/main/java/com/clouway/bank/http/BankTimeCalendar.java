package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Timestamp manipulation and retrieval of the Calendar
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class BankTimeCalendar implements BankCalendar {

  private Calendar calendar;
  private int delayInMinutes;

  public BankTimeCalendar(Calendar calendar, int delayInMinutes) {
    this.calendar = calendar;
    this.delayInMinutes = delayInMinutes;
  }

  /**
   * A timestamp for a given moment after a specified period of time
   *
   * @return the Long time
   */
  @Override
  public Long getExpirationTime() {
    calendar.setTime(new Date(System.currentTimeMillis()));
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + delayInMinutes);
    return calendar.getTimeInMillis();
  }


  /**
   * A Long time for the current moment
   *
   * @return the Long time
   */
  @Override
  public Long getCurrentTime() {
    calendar.setTime(new Date(System.currentTimeMillis()));
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }
}
