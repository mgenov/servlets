package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 19.05.16.
 */
public class SecurityFilter implements Filter {

  private SessionRepository sessionRepository;
  private CookieFinder cookieFinder;

  public SecurityFilter(SessionRepository sessionRepository, CookieFinder cookieFinder) {
    this.sessionRepository = sessionRepository;
    this.cookieFinder = cookieFinder;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    sessionRepository.cleanExpired();
    Cookie[] cookies = request.getCookies();
    Cookie cookie = cookieFinder.find(cookies);

    if(cookie==null){
      response.sendRedirect("/login?errorMsg=Session expired! Please log in again!");
      return;
    }
    String sessionID = cookie.getValue();
    Session session = sessionRepository.get(sessionID);

    if (session != null) {
      sessionRepository.refreshSessionTime(session);
      filterChain.doFilter(request, response);
      return;
    }
    response.sendRedirect("/login?errorMsg=Session expired! Please log in again!");
  }


  public void destroy() {
  }
}
