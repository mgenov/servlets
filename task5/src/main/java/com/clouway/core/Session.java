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
}
