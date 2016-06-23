package com.clouway.bank.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class AccountRepositoryUtility {
  private Connection connection;

  public AccountRepositoryUtility(Connection connection) {
    this.connection = connection;
  }

  public void instantiateAccount(String username) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement("INSERT INTO account VALUES (?, ?)");
      preparedStatement.setString(1, username);
      preparedStatement.setDouble(2, 0);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void clearAccountTable() {
    try {
      Statement statement = connection.createStatement();
      statement.execute("DELETE FROM account;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
