package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 08.06.16.
 */
public class LoginFilterTest {
  private LoginFilter loginFilter;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  SessionRepository sessionRepository;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  CookieFinder cookieFinder;

  @Mock
  FilterChain filterChain;

  @Before
  public void setUp() {
    loginFilter = new LoginFilter(sessionRepository, cookieFinder);
  }

  @Test
  public void userLogged() throws Exception {
    final Cookie[] cookies = new Cookie[]{};
    final Cookie cookie = new Cookie("sessionId", "3131-3344-6667-8843");
    final Session session = new Session("krisko@abv.bg", "3131-3344-6667-8843");

    context.checking(new Expectations() {{
      oneOf(sessionRepository).cleanExpired();
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).get(cookie.getValue());
      will(returnValue(session));

      oneOf(sessionRepository).refreshSessionTime(session);

      oneOf(response).sendRedirect("/useraccount");
    }});

    loginFilter.doFilter(request, response, filterChain);
  }

  @Test
  public void userNotLogged() throws ServletException, IOException {
    final Cookie[] cookies = new Cookie[]{};

    context.checking(new Expectations() {{
      oneOf(sessionRepository).cleanExpired();
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(null));

      oneOf(filterChain).doFilter(request, response);
    }});

    loginFilter.doFilter(request, response, filterChain);
  }

  @Test
  public void userDoesNotExistInSessionDb() throws Exception {
    final Cookie[] cookies = new Cookie[]{};
    final Cookie cookie = new Cookie("sessionId", "3131-3344-6667-8843");

    context.checking(new Expectations() {{
      oneOf(sessionRepository).cleanExpired();
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).get(cookie.getValue());
      will(returnValue(null));

      oneOf(response).sendRedirect("/login");
    }});
    loginFilter.doFilter(request, response, filterChain);
  }
}