package com.clouway.bank.persistence;

import com.clouway.bank.core.Provider;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  private final Provider<Connection> provider;

  public PersistentUserRepository(Provider<Connection> provider) {
    this.provider = provider;
  }

  @Override
  public void register(User user) {
    try (PreparedStatement statement = provider.get().prepareStatement("INSERT INTO users VALUES (?,?)")) {
      statement.setString(1, user.name);
      statement.setString(2, user.password);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
