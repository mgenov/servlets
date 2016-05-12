package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class CurrentSessionProvider implements SessionProvider {
  private Session session;

  public void set(Session newSession) {
    this.session = newSession;
  }

  public Session get() {
    return session;
  }
}
