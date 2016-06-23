package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.http.ConnectionFilter;

import java.sql.Connection;

/**
 * Gets connection to the database
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PerRequestConnectionProvider implements ConnectionProvider {

  /**
   * gets connection to the database from the ConnectionFilter
   *
   * @return Connection to the database
   */
  @Override
  public Connection get() {
    return ConnectionFilter.getConnection();
  }
}
