package com.clouway.persistent.adapter.jdbc;

import com.clouway.core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {

  @Override
  public Connection get() {
    String host = System.getenv("BANK_DB_HOST");
    String user = System.getenv("BANK_DB_USER");
    String pass = System.getenv("BANK_DB_PASS");

    Connection connection = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://" + host + "/myBank" + "?autoReconnect=true&useSSL=false", user, pass);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("The MySQL JDBC driver wasn't configured");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
}