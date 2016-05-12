package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {

  private ConnectionProvider connectionProvider;

  public PersistentUserRepository(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  /**
   * Registers the user
   *
   * @param user the user object
   */
  @Override
  public void register(User user) {
    String query = "INSERT INTO users(username, password) VALUES(?, ?);";
    Connection connection = connectionProvider.get();

    if (getUserById(user.id) != null) {
      throw new ValidationException("username is taken");
    }

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, user.id);
      preparedStatement.setString(2, user.password);
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();

    }
  }


  /**
   * Finds user by it's name
   *
   * @param userId the user name
   * @return the user
   */
  public User getUserById(String userId) {
    String query = "SELECT * FROM users WHERE username = ?;";
    Connection connection = connectionProvider.get();

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      String username = resultSet.getString(1);
      String password = resultSet.getString(2);
      return new User(username, password);
    } catch (SQLException e) {
      return null;
    }
  }
}
