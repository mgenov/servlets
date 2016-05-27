package com.clouway.adapter.http;

import com.clouway.core.OnlineUsers;
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

  public LogoutController(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Cookie[] cookies = req.getCookies();
    for (int i = 0; i < cookies.length; i++) {
      if (cookies[i].getName().equals("sessionId")) {
        sessionRepository.deleteSession(cookies[i].getValue());
        cookies[i].setMaxAge(0);
        resp.addCookie(cookies[i]);
        resp.sendRedirect("/login");
      }
    }
  }
}
