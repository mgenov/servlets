package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.UserValidator;
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
  private UserValidator validator;

  public PersistentUserRepository(ConnectionProvider connectionProvider, UserValidator validator) {
    this.connectionProvider = connectionProvider;
    this.validator = validator;
  }

  /**
   * Registers the user
   *
   * @param user            the user object
   * @param confirmPassword confirming the password
   */
  @Override
  public void register(User user, String confirmPassword) {
    String query = "Insert into users(username, password) values(?, ?);";
    Connection connection = connectionProvider.get();

    String validationMessage = validator.validate(user);
    validationMessage += validator.passwordsMatch(user.password, confirmPassword);

    if (!("".equals(validationMessage))) {
      throw new  ValidationException(validationMessage);
    }

    if(getUserById(user.username) != null ){
      throw new ValidationException("username is taken");
    }

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, user.username);
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
    String query = "Select * from users where username = ?;";
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
