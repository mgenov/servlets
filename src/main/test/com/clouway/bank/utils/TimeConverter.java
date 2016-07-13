package com.clouway.bank.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class TimeConverter {
  public long convertStringToLong(String timeAsString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ssss");

    Date date = null;
    try {
      date = simpleDateFormat.parse(timeAsString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
