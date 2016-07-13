package com.clouway.bank.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DateConverter {
  public long convertStringToLong(String dateFormat, String dateAsString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    try {
      date = simpleDateFormat.parse(dateAsString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
