package com.clouway.bank.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface RowFetcher<T> {

  /**
   * Fetcher a row out of the result set.
   *
   * @param rs the result set
   * @return returns object of type T, containing the row data
   * @throws SQLException
   */
  T fetchRow(ResultSet rs) throws SQLException;
}
