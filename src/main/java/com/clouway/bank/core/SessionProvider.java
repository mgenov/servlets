package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface SessionProvider {

  void set(Session session);

  Session get();
}
