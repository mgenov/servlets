package com.clouway.bank.core;

import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class CurrentDateImplementation implements CurrentDate {
  @Override
  public long getCurrentDate() {
    return new Date().getTime();
  }
}

