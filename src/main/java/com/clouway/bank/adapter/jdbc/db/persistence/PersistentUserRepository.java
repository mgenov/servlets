package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  private Provider<Connection> connectionProvider;

  public PersistentUserRepository(Provider<Connection> connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  @Override
  public void register(User user) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("INSERT INTO users VALUES (?,?,?)")) {
      statement.setString(1, user.name);
      statement.setString(2, user.email);
      statement.setString(3, user.password);

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public User findByEmail(String email) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("SELECT * FROM users WHERE email=?")) {
      statement.setString(1, email);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {

        String name = resultSet.getString("name");
        String password = resultSet.getString("password");

        return new User(name, email, password);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return null;
  }
}

