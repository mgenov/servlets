package com.clouway.bank.core;

import java.util.UUID;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Session {
  public final String sessionId;
  public final String email;
  public final long time;

  public Session(String sessionId, String email, long time) {
    this.sessionId = sessionId;
    this.email = email;
    this.time = time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Session session = (Session) o;

    if (time != session.time) return false;
    if (sessionId != null ? !sessionId.equals(session.sessionId) : session.sessionId != null) return false;
    return email != null ? email.equals(session.email) : session.email == null;

  }

  @Override
  public int hashCode() {
    int result = sessionId != null ? sessionId.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (int) (time ^ (time >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Session{" +
            "sessionId='" + sessionId + '\'' +
            ", email='" + email + '\'' +
            ", time=" + time +
            '}';
  }
}
