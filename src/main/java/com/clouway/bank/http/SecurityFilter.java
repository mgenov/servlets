package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionProvider;
import com.clouway.bank.core.SessionRepository;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class SecurityFilter implements Filter {
  private SessionRepository sessionRepository;
  private BankCalendar bankCalendar;
  private List<String> unsecuredPages;
  private SessionProvider sessionProvider;

  public SecurityFilter(SessionRepository sessionRepository, BankCalendar bankCalendar, List<String> unsecuredPages, SessionProvider sessionProvider) {
    this.sessionRepository = sessionRepository;
    this.bankCalendar = bankCalendar;
    this.unsecuredPages = unsecuredPages;
    this.sessionProvider = sessionProvider;
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    String sessionId = getSid(request);
    final String uri = request.getRequestURI();
    boolean uriAccessible = unsecuredPages.contains(uri);

    if (sessionId == null && !uriAccessible) {
      response.sendRedirect("/login");
      return;
    }

    if (sessionId == null && uriAccessible) {
      chain.doFilter(request, response);
      return;
    }

    Optional<Session> session = Optional.ofNullable(sessionRepository.retrieve(sessionId));
    boolean sessionActive = session.isPresent() && session.get().expirationTime > bankCalendar.getCurrentTime();

    if (uriAccessible && sessionActive) {
      response.sendRedirect("/");
      return;
    }
    if (!uriAccessible && sessionActive) {
      Session updatedSession = new Session(session.get().id, session.get().userId, bankCalendar.getExpirationTime());
      sessionRepository.update(updatedSession);
      sessionProvider.set(updatedSession);
      chain.doFilter(request, response);
      return;
    }
    if (uriAccessible || sessionActive) {
      chain.doFilter(request, response);
    } else {
      response.sendRedirect("/login");
    }
  }

  private String getSid(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    String sessionId = null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("sessionId")) {
        sessionId = cookie.getValue();
      }
    }
    return sessionId;
  }

  public void init(FilterConfig config) throws ServletException {

  }

}
