package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.*;
import com.google.common.base.Optional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentSessionRepository implements SessionRepository {
  private final Provider<Connection> connectionProvider;

  public PersistentSessionRepository(Provider<Connection> provider) {
    this.connectionProvider = provider;
  }

  @Override
  public void createSession(Session session) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("INSERT into sessions VALUES (?,?,?)")) {
      statement.setString(1, session.sessionId);
      statement.setString(2, session.email);
      statement.setLong(3, session.sessionLifeTime);

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public Optional<Session> findSessionById(String id) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("SELECT * FROM sessions WHERE id=?")) {
      statement.setString(1, id);

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        String email = resultSet.getString("email");
        long timeout = resultSet.getLong("sessionLifeTime");
        Session session = new Session(id, email, timeout);

        return Optional.of(session);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return Optional.absent();
  }


  @Override
  public void remove(String id) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("DELETE FROM sessions WHERE id=?")) {
      statement.setString(1, id);

      statement.execute();
    } catch (SQLException e) {
      new ConnectionException("Cannot connect to database");
    }
  }
}
