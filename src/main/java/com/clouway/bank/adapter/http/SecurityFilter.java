package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
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

  public SecurityFilter(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    Cookie cookie = find(request.getCookies());

    final String uri = request.getRequestURI();

    Optional<Session> currentUser = null;
    if (cookie != null) {
      currentUser = sessionRepository.findSessionById(cookie.getValue());
    }
    if (uri.contains("/login") || currentUser.isPresent()) {
      filterChain.doFilter(request, response);

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
