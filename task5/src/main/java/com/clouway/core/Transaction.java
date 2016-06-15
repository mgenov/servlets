package com.clouway.core;

import java.util.Date;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 14.06.16.
 */
public class Transaction {
  public Integer ID;
  public String date;
  public String email;
  public String operation;
  public Double amount;

  public Transaction(Integer ID, String date, String email, String operation, Double amount) {
    this.ID = ID;
    this.date = date;
    this.email = email;
    this.operation = operation;
    this.amount = amount;
  }
}
