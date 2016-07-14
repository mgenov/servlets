package com.clouway.bank.utils;

import javax.servlet.http.Cookie;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SessionIdFinder {
  private final String sidCookie;

  public SessionIdFinder(String sidCookie) {
    this.sidCookie = sidCookie;
  }

  public String findSid(Cookie[] cookies) {
    String sessionId = "";
    for (Cookie each : cookies) {
      if (each.getName().equals(sidCookie)) {
        sessionId = each.getValue();
      }
    }
    return sessionId;
  }
}
