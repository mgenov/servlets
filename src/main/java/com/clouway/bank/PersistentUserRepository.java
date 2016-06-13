package com.clouway.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  public void register(User user) {
    try {
      DriverManager.registerDriver(new org.postgresql.Driver());
      Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/users", "postgres", "clouway.com");
      PreparedStatement statement = connection.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?);");
      statement.setString(1, user.name);
      statement.setString(2, user.password);
      statement.execute();
      connection.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public User findByName(String userName) {
    try {
      DriverManager.registerDriver(new org.postgresql.Driver());
      Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/users", "postgres", "clouway.com");
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=?");
      statement.setString(1, userName);
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      String password = resultSet.getString("password");
      if (password==null|| "".equals(password)){
        return null;
      }else {
        return new User(userName, password);
      }
    } catch (SQLException e) {
      return null;
    }
  }
}
