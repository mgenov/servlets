package com.clouway.bank.core;

import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface TransactionRepository {
  /**
   * Will update history
   *
   * @param email     user email
   * @param operation current operation
   * @param amount    processing ammount
   */
  void updateHistory(String email, String operation, Double amount);

  /**
   * Will get all records from transactions
   *
   * @param email  email of the user
   * @param limit  how mani records retrieve
   * @param offset from where retrieve records
   * @return list with records
   */

  List<Transaction> getTransactions(String email, int limit, int offset);

  /**
   * Will get number of all records
   *
   * @return number of records
   */
  int getNumberOfRecords();
}
