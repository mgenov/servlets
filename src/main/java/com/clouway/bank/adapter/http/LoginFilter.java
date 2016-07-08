package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Time;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginFilter implements Filter {
  private final SessionRepository sessionRepository;
  private final Time time;

  public LoginFilter(SessionRepository sessionRepository, Time time) {
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

    Cookie[] cookies = request.getCookies();
    Cookie sessionId = find(cookies);

    Session session = null;

    if (sessionId == null) {
      filterChain.doFilter(request, response);
      return;
    }

    session = sessionRepository.findSessionById(sessionId.getValue());

    if (session != null) {
      if (session.timeForLife < time.getCurrentTime()) {
        sessionId.setMaxAge(-1);
        sessionRepository.remove(session.sessionId);
      }
    } else {
      response.sendRedirect("/login");
    }
  }

  @Override
  public void destroy() {

  }

  private Cookie find(Cookie[] cookies) {
    for (Cookie each : cookies) {
      if (each.getName().equals("id")) {
        return each;
      }
    }
    return null;
  }
}