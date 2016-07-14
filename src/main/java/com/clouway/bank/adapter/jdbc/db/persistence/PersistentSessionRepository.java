package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
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
      statement.setLong(3, session.expirationTime);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
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
        long expirationTime = resultSet.getLong("expirationTime");
        Session session = new Session(id, email, expirationTime);

        return Optional.of(session);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return Optional.absent();
  }


  @Override
  public void remove(String sessionId) {
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("DELETE FROM sessions WHERE id=?")) {
      statement.setString(1, sessionId);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public int getOnlineUsersCount() {
    int counter = 0;
    try (PreparedStatement statement = connectionProvider.get().prepareStatement("SELECT COUNT (DISTINCT (email))FROM sessions")) {

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        counter = resultSet.getInt(1);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return counter;
  }
}
