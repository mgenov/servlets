package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentSessionRepository implements SessionRepository {
  private final Provider<Connection> provider;

  public PersistentSessionRepository(Provider<Connection> provider) {
    this.provider = provider;
  }

  @Override
  public void save(Session session) {
    try (PreparedStatement statement = provider.get().prepareStatement("INSERT into sessions VALUES (?,?,?)")) {
      statement.setString(1, session.sessionId);
      statement.setString(2, session.email);
      statement.setLong(3, session.time);

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public Session findSession(String email) {
    try (PreparedStatement statement = provider.get().prepareStatement("SELECT * FROM sessions WHERE email=?")) {
      statement.setString(1, email);

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        String id = resultSet.getString("id");
        long time = resultSet.getLong("time");

        return new Session(id, email, time);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return null;
  }
}
