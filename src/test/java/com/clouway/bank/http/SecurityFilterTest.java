package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;
import com.clouway.bank.core.CurrentSessionProvider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
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
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class SecurityFilterTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  SessionRepository sessionRepository;
  @Mock
  BankCalendar bankCalendar;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  FilterChain chain;
  @Mock
  List<String> accessibleLinks;
  private SecurityFilter securityFilter;

  @Before
  public void setUp() {
    CurrentSessionProvider currentSessionProvider = new CurrentSessionProvider();
    securityFilter = new SecurityFilter(sessionRepository, bankCalendar, accessibleLinks, currentSessionProvider);
  }

  @Test
  public void happyPath() throws ServletException, IOException {
    Cookie[] cookies = new Cookie[]{};
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getRequestURI();
      will(returnValue("/login"));

      oneOf(accessibleLinks).contains("/login");
      will(returnValue(true));

      oneOf(chain).doFilter(request, response);
    }});

    securityFilter.doFilter(request, response, chain);
  }

  @Test
  public void noCookie() throws ServletException, IOException {
    Cookie[] cookies = new Cookie[]{};
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getRequestURI();
      will(returnValue("/deposit"));

      oneOf(accessibleLinks).contains("/deposit");
      will(returnValue(false));

      oneOf(response).sendRedirect("/login");
    }});

    securityFilter.doFilter(request, response, chain);
  }

  @Test
  public void sessionOver() throws ServletException, IOException {
    Cookie[] cookies = new Cookie[]{new Cookie("sessionId", "1")};
    Session session = new Session("1", "Krasimir", 1L);
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getRequestURI();
      will(returnValue("/deposit"));

      oneOf(sessionRepository).retrieve("1");
      will(returnValue(session));

      oneOf(accessibleLinks).contains("/deposit");
      will(returnValue(false));

      oneOf(bankCalendar).getCurrentTime();
      will(returnValue(2L));

      oneOf(response).sendRedirect("/login");
    }});

    securityFilter.doFilter(request, response, chain);
  }

  @Test
  public void accessingLoginWhenLogedin() throws IOException, ServletException {
    Cookie[] cookies = new Cookie[]{new Cookie("sessionId", "1")};
    Session session = new Session("1", "Krasimir", 2L);
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getRequestURI();
      will(returnValue("/login"));

      oneOf(accessibleLinks).contains("/login");
      will(returnValue(true));

      oneOf(sessionRepository).retrieve("1");
      will(returnValue(session));

      oneOf(bankCalendar).getCurrentTime();
      will(returnValue(1L));

      oneOf(response).sendRedirect("/");
    }});

    securityFilter.doFilter(request, response, chain);
  }

  @Test
  public void accessingDepositWithExpiredCookie() throws ServletException, IOException {
    Cookie[] cookies = new Cookie[]{new Cookie("sessionId", "1")};
    Session session = new Session("1", "Krasimir", 2L);
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getRequestURI();
      will(returnValue("/deposit"));

      oneOf(accessibleLinks).contains("/deposit");
      will(returnValue(false));

      oneOf(sessionRepository).retrieve("1");
      will(returnValue(session));

      oneOf(bankCalendar).getCurrentTime();
      will(returnValue(3L));

      oneOf(response).sendRedirect("/login");
    }});

    securityFilter.doFilter(request, response, chain);
  }

}