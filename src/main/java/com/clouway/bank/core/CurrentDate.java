package com.clouway.bank.core;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class CurrentDate implements Date {
  @Override
  public long getCurrentDate() {
    return new java.util.Date().getTime();
  }
}

