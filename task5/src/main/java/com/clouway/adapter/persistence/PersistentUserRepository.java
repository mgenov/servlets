package com.clouway.adapter.persistence;

import com.clouway.core.User;
import com.clouway.core.ConnectionProvider;
import com.clouway.core.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 26.05.16.
 */
public class PersistentUserRepository implements UserRepository {
  private ConnectionProvider connectionProvider;
  private Connection connection;

  public PersistentUserRepository(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  public PersistentUserRepository(Connection connection) {
    this.connection = connection;
  }

  public void register(User user) {
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("INSERT INTO users (username,password,email) VALUES (?,?,?)");
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.execute();
    } catch (SQLException sql) {
      sql.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public User findByEmail(String email) {
    Connection connection = connectionProvider.get();
    User user = null;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        user = new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
      }
      resultSet.close();
    } catch (SQLException sql) {
      sql.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return user;
  }

  public boolean authenticate(String email, String password) {
    Connection connection = connectionProvider.get();
    boolean result = false;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
      statement.setString(1, email);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        result = true;
      }
      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public void deleteAll() {
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("DELETE FROM users");
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}

