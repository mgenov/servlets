package com.clouway.bank.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class SessionRepositoryUtility {

  private Connection connection;

  public SessionRepositoryUtility(Connection connection) {
    this.connection = connection;
  }

  public void clearSessionTable() {
    try {
      Statement statement = connection.createStatement();
      statement.execute("DELETE FROM login;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
