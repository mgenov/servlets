package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.CurrentTime;
import com.google.common.base.Optional;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SecurityFilter implements Filter {
  private final SessionRepository sessionRepository;
  private final CurrentTime time;

  public SecurityFilter(SessionRepository sessionRepository, CurrentTime time) {
    this.sessionRepository = sessionRepository;
    this.time = time;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    final String uri = request.getRequestURI();

    String sessionId = findSid(request.getCookies());

    Optional<Session> currentSession = sessionRepository.findSessionById(sessionId);
    if (uri.contains("/login") && currentSession.isPresent()) {
      response.sendRedirect("/home");
    }
    if (isExpireOff(currentSession)) {
      sessionRepository.remove(sessionId);
    }
    if (uri.contains("/login") || currentSession.isPresent()) {
      filterChain.doFilter(request, response);
    } else {
      response.sendRedirect("/login");
    }
  }

  private boolean isExpireOff(Optional<Session> currentSession) {
    if (currentSession.isPresent() && currentSession.get().expirationTime < time.getCurrentTime()) {
      return true;
    }
    return false;
  }

  @Override
  public void destroy() {

  }

  private String findSid(Cookie[] cookies) {
    String sessionId = "";
    for (Cookie each : cookies) {
      if (each.getName().equals("sessionId")) {
        sessionId = each.getValue();
      }
    }
    return sessionId;
  }
}
