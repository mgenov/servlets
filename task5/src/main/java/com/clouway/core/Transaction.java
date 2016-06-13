package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 14.06.16.
 */
public class Transaction {
  public int ID;
  public String date;
  public String email;
  public String operation;
  public double amount;

  public Transaction(int ID, String date, String email, String operation, double amount) {
    this.ID = ID;
    this.date = date;
    this.email = email;
    this.operation = operation;
    this.amount = amount;
  }
}
