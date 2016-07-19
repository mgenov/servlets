package com.clouway.bank.core;


import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface DataStore {

  /**
   * Executes query to some repository
   *
   * @param query   the query itself
   * @param fetcher the row fetcher to fetch row from the results
   * @param params  the parameters to the query
   * @param <T>     the return Type
   * @return returns a List of T
   */
  <T> List<T> fetchRows(String query, RowFetcher<T> fetcher, Object... params);

  /**
   * Executes some manipulation query like insert, update and delete that do not return any thing
   *
   * @param query  the query
   * @param params the parameters for the query
   */
  void executeQuery(String query, Object... params);
}
