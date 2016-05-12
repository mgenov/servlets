package com.clouway.bank.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class LoginRepositoryUtility {

  private Connection connection;

  public LoginRepositoryUtility(Connection connection) {
    this.connection = connection;
  }

  public void clearLoginTable() {
    try {
      Statement statement = connection.createStatement();
      statement.execute("DELETE FROM login;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
