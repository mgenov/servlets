package com.clouway.bank.utils;

import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;

import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Pager {
  private TransactionRepository transactionRepository;
  private int pageSize;
  private final String email;

  public Pager(TransactionRepository transactionRepository, int pageSize, String email) {
    this.email = email;
    this.transactionRepository = transactionRepository;
    this.pageSize = pageSize;
  }

  public List<Transaction> getPage(int currentPageNumber) {
    return transactionRepository.getTransactions(email, pageSize, (currentPageNumber - 1) * pageSize);
  }

  public Integer getPreviousPageNumber(int currentPageNumber) {
    Integer previous;
    if (currentPageNumber == 1) {
      return 1;
    }
    previous = currentPageNumber - 1;
    return previous;
  }

  public Integer getNextPageNumber(int currentPageNumber) {
    Integer next;
    if (!hasNext(currentPageNumber)) {
      return next = currentPageNumber;

    } else {
      next = currentPageNumber + 1;
    }
    return next;
  }

  public boolean hasNext(int currentPageNumber) {
    return currentPageNumber < getNumberOfPages();
  }

  private int getNumberOfPages() {
    Integer totalNumberOfRecords = transactionRepository.getNumberOfRecords();

    if (totalNumberOfRecords % pageSize != 0) {
      return (totalNumberOfRecords % pageSize);

    } else {
      return totalNumberOfRecords / pageSize;
    }
  }
}
