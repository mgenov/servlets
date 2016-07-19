package com.clouway.bank.persistent;

import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * repository for storing logins
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentSessionRepository implements SessionRepository {
  private DataStore dataStore;

  public PersistentSessionRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  /**
   * Create some session into the repository
   *
   * @param session the session to be added
   */
  @Override
  public void create(Session session) {
    String query = "INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)";

    dataStore.executeQuery(query, session.id, session.userId, new Timestamp(session.expirationTime));
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

    RowFetcher<Session> rowFetcher = rs -> new Session(rs.getString("sessionid"), rs.getString("username"), rs.getTimestamp("expirationtime").getTime());
    List<Session> sessions = new ArrayList();
    sessions = dataStore.fetchRows(query, rowFetcher, sessionId);
    if (sessions.isEmpty()) {
      return null;
    }
    return sessions.get(0);
  }

  /**
   * Updates the session by it's id
   *
   * @param session the updated version of the session
   */
  @Override
  public void update(Session session) {
    String query = "UPDATE login SET expirationtime=? WHERE sessionid=?;";

    dataStore.executeQuery(query, new Timestamp(session.expirationTime), session.id);
  }

  @Override
  public Integer countActive(Long currentTime) {
    String query = "SELECT count(*) FROM login WHERE expirationtime>?;";

    RowFetcher<Integer> rowFetcher = rs -> rs.getInt(1);

    List<Integer> amountActiveSessions = dataStore.fetchRows(query, rowFetcher, new Timestamp(currentTime));
    if (amountActiveSessions.isEmpty()) {
      return null;
    }
    return amountActiveSessions.get(0);
  }

  @Override
  public void remove(String sessionId) {
    String query = "DELETE FROM login WHERE sessionid=?;";

    dataStore.executeQuery(query, sessionId);
  }
}
