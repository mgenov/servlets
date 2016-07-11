package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.LoginException;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * repository for storing logins
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentSessionRepository implements SessionRepository {
  private ConnectionProvider connectionProvider;

  public PersistentSessionRepository(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  /**
   * Create some session into the repository
   *
   * @param session the session to be added
   */
  @Override
  public void create(Session session) {
    String query = "INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)";
    Connection connection = connectionProvider.get();

    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, session.id);
      statement.setString(2, session.userId);
      statement.setTimestamp(3, new Timestamp(session.expirationTime));
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new LoginException("There was a problem with your session, please try again, and if needed contact the administrators");
    }
  }

  /**
   * Retrieves login from the repository be given id
   *
   * @param sessionId the id of the login
   * @return the Session
   */
  @Override
  public Session retrieve(String sessionId) {
    String query = "SELECT sessionid, username, expirationtime FROM login WHERE sessionid=?;";

    Connection connection = connectionProvider.get();

    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, sessionId);
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      return new Session(resultSet.getString("sessionid"), resultSet.getString("username"), resultSet.getTimestamp("expirationtime").getTime());
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Updates the session by it's id
   *
   * @param session the updated version of the session
   */
  @Override
  public void update(Session session) {
    String query = "UPDATE login SET expirationtime=? WHERE sessionid=?;";

    Connection connection = connectionProvider.get();

    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setTimestamp(1, new Timestamp(session.expirationTime));
      statement.setString(2, session.id);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new LoginException("There was a problem with your session, please try to login again, if needed contact the administrators");
    }
  }

  @Override
  public Integer countActive(Long currentTime) {
    String query = "SELECT count(*) FROM login WHERE expirationtime>?;";
    Connection connection = connectionProvider.get();

    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setTimestamp(1, new Timestamp(currentTime));
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      return resultSet.getInt(1);
    } catch (SQLException e) {
      return null;
    }
  }

  @Override
  public void remove(String sessionId) {
    String query = "DELETE FROM login WHERE sessionid=?;";

    Connection connection = connectionProvider.get();

    try {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, sessionId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
