package com.clouway.core;

import java.util.List;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 09.06.16.
 */
public interface FundsRepository {

  void deposit(double amount, String email);

  boolean withdraw(Double amount, String email);

  double getBalance(String email);

  void updateHistory(String email, String operation, Double amount);

  List<Transaction> getHistory(Integer limit, Integer offset);
}
