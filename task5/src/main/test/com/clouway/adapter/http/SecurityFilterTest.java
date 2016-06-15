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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public class SecurityFilterTest {
  private SecurityFilter securityFilter;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  SessionRepository sessionRepo;

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
    securityFilter = new SecurityFilter(sessionRepo, cookieFinder);
  }

  @Test
  public void happyPath() throws Exception {
    final Cookie[] cookies = new Cookie[]{};
    final Cookie cookie = new Cookie("sessionId", "3131-3344-6667-8843");
    final Session session = new Session("krisko@abv.bg", "3131-3344-6667-8843");

    context.checking(new Expectations() {{
      oneOf(sessionRepo).cleanExpired();

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepo).get(cookie.getValue());
      will(returnValue(session));

      oneOf(sessionRepo).refreshSessionTime(session);

      oneOf(filterChain).doFilter(request, response);
    }});

    securityFilter.doFilter(request, response, filterChain);
  }

  @Test
  public void sessionNotFound() throws Exception {
    final Cookie[] cookies = new Cookie[]{};
    final Cookie cookie = new Cookie("sessionId", "3131-3344-6667-8843");

    context.checking(new Expectations() {{
      oneOf(sessionRepo).cleanExpired();

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepo).get(cookie.getValue());
      will(returnValue(null));

      oneOf(response).sendRedirect("/login?errorMsg=Session expired! Please log in again!");
    }});

    securityFilter.doFilter(request, response, filterChain);
  }
}
