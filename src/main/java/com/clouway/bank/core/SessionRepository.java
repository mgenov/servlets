package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to save and retrieve session data
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface SessionRepository {
  /**
   * Will save session information
   *
   * @param session session
   */
  void save(Session session);

  /**
   * find session by email to user
   *
   * @param email user email
   * @return session
   */
  Session findSession(String email);
}
