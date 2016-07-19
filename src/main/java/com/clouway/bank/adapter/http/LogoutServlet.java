package com.clouway.bank.adapter.http;

import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.SessionIdFinder;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LogoutServlet extends HttpServlet {
  private final SessionIdFinder sessionIdFinder;
  private final SessionRepository sessionRepository;

  public LogoutServlet(SessionIdFinder sessionIdFinder, SessionRepository sessionRepository) {
    this.sessionIdFinder = sessionIdFinder;
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String sessionId = sessionIdFinder.findSid(req.getCookies());

    if (!Strings.isNullOrEmpty(sessionId)) {
      sessionRepository.remove(sessionId);
      resp.sendRedirect("/login");
    }
  }
}
