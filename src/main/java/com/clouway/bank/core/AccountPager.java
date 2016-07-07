package com.clouway.bank.core;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface AccountPager {

  /**
   * Gives the current page number of the pager
   *
   * @return the current apge
   */
  int getCurrentPageNumber();


  /**
   * Gives the previous page number
   *
   * @return
   */
  int getPreviousPageNumber();

  /**
   * Gives the next page number
   *
   * @return
   */
  int getNextPageNumber();

  /**
   * makes a request for a page, if this page exists it is being returned,
   * else the one closest to it is.
   *
   * @param userId     the id of the user
   * @param pageNumber the number of the page
   * @return the account records on that page
   */
  List<AccountRecord> requestPage(String userId, int pageNumber);

  /**
   * counts the pages for the given user
   *
   * @param userId the user identification
   * @return the number of pages
   */
  int countPages(String userId);
}
