package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Session {
  public final String id;
  public final String userId;
  public final Long expirationTime;

  public Session(String id, String userId, Long expirationTime) {
    this.id = id;
    this.userId = userId;
    this.expirationTime = expirationTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Session session = (Session) o;

    if (id != null ? !id.equals(session.id) : session.id != null) return false;
    if (userId != null ? !userId.equals(session.userId) : session.userId != null) return false;
    return !(expirationTime != null ? !expirationTime.equals(session.expirationTime) : session.expirationTime != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (expirationTime != null ? expirationTime.hashCode() : 0);
    return result;
  }

}
