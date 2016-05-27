package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 02.06.16.
 */
public interface SessionRepository {

  void createSession(Session session);

  Session getSession(String sessionID);

  void deleteSession(String sessionID);
}
