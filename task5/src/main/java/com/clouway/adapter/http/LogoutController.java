package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.SessionRepository;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 02.06.16.
 */
public class LogoutController extends HttpServlet {
  private SessionRepository sessionRepository;
  private CookieFinder cookieFinder;

  public LogoutController(SessionRepository sessionRepository, CookieFinder cookieFinder) {
    this.sessionRepository = sessionRepository;
    this.cookieFinder = cookieFinder;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Cookie cookie = cookieFinder.find(req.getCookies());
    if (cookie != null) {
      sessionRepository.delete(cookie.getValue());
      cookie.setMaxAge(0);
      resp.addCookie(cookie);
    }
    resp.sendRedirect("/login");
  }
}
