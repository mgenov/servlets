package com.clouway.bank.core;

import java.sql.Connection;

/**
 * Will provide connections to the database
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface ConnectionProvider {

  /**
   * Gets connection
   *
   * @return connection to the database
   */
  Connection get();
}
