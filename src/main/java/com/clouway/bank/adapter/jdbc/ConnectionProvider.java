package com.clouway.bank.adapter.jdbc;

import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ConnectionProvider implements Provider {
  private final String db;
  private final String user;
  private final String password;

  public ConnectionProvider(String db, String user, String password) {
    this.db = db;
    this.user = user;
    this.password = password;
  }

  @Override
  public Connection get() {
    try {
      return DriverManager.getConnection(db, user, password);
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }
}
