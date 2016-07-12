package com.clouway.bank.utils;

import com.clouway.bank.core.SessionTime;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Timeout implements SessionTime {
  private final int minute;

  public Timeout(int minute) {
    this.minute = minute;
  }

  @Override
  public long getCurrentTime() {
    return new Date().getTime();
  }

  @Override
  public long getTimeOfLife() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(getCurrentTime()));
    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);

    return calendar.getTimeInMillis();
  }
}
