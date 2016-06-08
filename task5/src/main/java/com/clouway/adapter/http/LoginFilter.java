package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 08.06.16.
 */
@WebFilter(filterName = "LoginFilter")
public class LoginFilter implements Filter {
  private SessionRepository sessionRepository;
  private CookieFinder cookieFinder;

  public LoginFilter(SessionRepository sessionRepository, CookieFinder cookieFinder) {
    this.sessionRepository = sessionRepository;
    this.cookieFinder = cookieFinder;
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    sessionRepository.cleanExpired();
    Cookie cookie=null;
    Cookie[] cookies = request.getCookies();
    if(cookies!=null) {
      cookie = cookieFinder.find(cookies);
    }

    if (cookie == null) {
      chain.doFilter(request, response);
      return;
    }

    String sessionID = cookie.getValue();
    Session session = sessionRepository.get(sessionID);

    if (session != null) {
      sessionRepository.refreshSessionTime(session);
      cookie.setMaxAge(300);
      response.sendRedirect("/useraccount");
      return;
    }
    response.sendRedirect("/login");
  }

  public void init(FilterConfig config) throws ServletException {

  }
}