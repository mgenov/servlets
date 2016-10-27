package com.clouway.persistent.adapter.jdbc;

import com.clouway.core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {
  private final String host;
  private final String user;
  private final String password;

  public ConnectionProvider(String host, String user, String password) {
    this.host = host;
    this.user = user;
    this.password = password;
  }

  @Override
  public Connection get() {
    Connection connection = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(host + "?autoReconnect=true&useSSL=false", user, password);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("The MySQL JDBC driver wasn't configured");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
}