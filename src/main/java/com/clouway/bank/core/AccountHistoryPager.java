package com.clouway.bank.core;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class AccountHistoryPager implements AccountPager {
  private int pageSize;
  private AccountHistoryRepository historyRepository;
  private int currentPage;
  private int previousPage;
  private int nextPage;

  public AccountHistoryPager(int pageSize, AccountHistoryRepository historyRepository) {
    this.pageSize = pageSize;
    this.historyRepository = historyRepository;
    currentPage = 0;
    previousPage = 0;
    nextPage = 1;
  }


  @Override
  public int getCurrentPageNumber() {
    return currentPage;
  }

  @Override
  public int getPreviousPageNumber() {
    return previousPage;
  }

  @Override
  public int getNextPageNumber() {
    return nextPage;
  }

  @Override
  public List<AccountRecord> requestPage(String userId, int pageNumber) {
    List<AccountRecord> records;

    if (pageNumber <= 0) {
      currentPage = 0;
      previousPage = 0;
      nextPage = 1;
      return historyRepository.getAccountRecords(userId, 0, pageSize);
    }

    records = historyRepository.getAccountRecords(userId, pageNumber * pageSize, pageSize + 1);

    if (records.size() == pageSize + 1) {
      currentPage = pageNumber;
      previousPage = currentPage - 1;
      nextPage = currentPage + 1;
    } else {
      currentPage = pageNumber;
      previousPage = currentPage - 1;
      nextPage = currentPage;
    }
    return records;
  }

  @Override
  public int countPages(String userId) {
    double records = historyRepository.countRecords(userId);
    int numberOfPages = (int) Math.ceil(records / pageSize);
    return numberOfPages;
  }

}
