package com.clouway.adapter.persistence;

import com.clouway.core.ConnectionProvider;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 02.06.16.
 */
public class PersistentSessionRepository implements SessionRepository {
  private ConnectionProvider connectionProvider;

  public PersistentSessionRepository(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  public void createSession(Session session) {
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("INSERT INTO session (email,sessionID) VALUES (?,?)");
      statement.setString(1, session.email);
      statement.setString(2, session.sessionId);
      statement.execute();
    } catch (SQLException sqlExc) {
      sqlExc.printStackTrace();
    } finally {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public Session getSession(String sessionID) {
    Session session = null;
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM session WHERE sessionID=?");
      statement.setString(1, sessionID);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        session = new Session(resultSet.getString("email"), resultSet.getString("sessionID"));
      }
      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return session;
  }

  public void deleteSession(String sessionID) {
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try{
      statement=connection.prepareStatement("DELETE FROM session WHERE sessionID=?");
      statement.setString(1,sessionID);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
