package com.clouway.bank.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserRepositoryUtility {
  private Connection connection;

  public UserRepositoryUtility(Connection connection) {
    this.connection = connection;
  }

  public void registerUser(String username, String password) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (?, ?)");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void clearUsersTable() {
    try {
      Statement statement = connection.createStatement();
      statement.execute("DELETE FROM users;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
