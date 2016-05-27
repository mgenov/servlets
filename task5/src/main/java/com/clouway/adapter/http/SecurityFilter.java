package com.clouway.adapter.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.User;
import com.clouway.core.UserRepository;

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

  public SecurityFilter(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    Cookie[] cookies = request.getCookies();
    String sessionID = "";
    for (int i = 0; i < cookies.length; i++) {
      if (cookies[i].getName().equals("sessionId")) {
        sessionID = cookies[i].getValue();
      }
    }
    Session session = sessionRepository.getSession(sessionID);
    if (session != null) {
      filterChain.doFilter(request, response);
    } else {
      response.sendRedirect("/login");
    }
  }


  public void destroy() {

  }
}
