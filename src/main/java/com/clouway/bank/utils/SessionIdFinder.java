package com.clouway.bank.utils;

import javax.servlet.http.Cookie;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SessionIdFinder {

  public String findSid(Cookie[] cookies) {
    String sessionId = "";
    for (Cookie each : cookies) {
      if (each.getName().equals("sessionId")) {
        sessionId = each.getValue();
      }
    }
    return sessionId;
  }
}
