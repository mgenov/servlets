package com.clouway.bank.core;

/**
 * repository for storing logins
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface SessionRepository {

  /**
   * Create some session into the repository
   *
   * @param session the session to be added
   */
  void create(Session session);

  /**
   * Retrieves login from the repository be given id
   *
   * @param sessionId the id of the login
   * @return the Session
   */
  Session retrieve(String sessionId);

  /**
   * Updates the session by it's id
   *
   * @param session the updated version of the session
   */
  void update(Session session);

  /**
   * Removes session from the repository
   *
   * @param sessionId the id of the session
   */
  void remove(String sessionId);

  /**
   * Counts the number of active sessions
   *
   * @param currentTime the current time in milliseconds
   * @return
   */
  Integer countActive(Long currentTime);
}
