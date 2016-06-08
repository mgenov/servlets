package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 02.06.16.
 */
public class Session {
  public final String email;
  public final String sessionId;


  public Session(String email, String sessionId) {
    this.email = email;
    this.sessionId = sessionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Session session = (Session) o;

    if (email != null ? !email.equals(session.email) : session.email != null) return false;
    return sessionId != null ? sessionId.equals(session.sessionId) : session.sessionId == null;

  }

  @Override
  public int hashCode() {
    int result = email != null ? email.hashCode() : 0;
    result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
    return result;
  }
}
