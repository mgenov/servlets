package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 02.06.16.
 */
public interface SessionRepository {

  void create(Session session);

  Session get(String sessionID);

  void delete(String sessionID);

  void deleteAll();

  void cleanExpired();

  void refreshSessionTime(Session session);

  String getCurrentUserEmail(String sessionId);

  Integer getActiveSessions();

}
