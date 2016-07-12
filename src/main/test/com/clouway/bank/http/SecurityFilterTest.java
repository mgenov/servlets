package com.clouway.bank.http;

import com.clouway.bank.adapter.http.SecurityFilter;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.CurrentTime;
import com.google.common.base.Optional;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SecurityFilterTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private FilterChain filterChain = context.mock(FilterChain.class);
  private CurrentTime currentTime = context.mock(CurrentTime.class);

  private SessionRepository sessionRepository = context.mock(SessionRepository.class);

  @Test
  public void login() throws Exception {
    SecurityFilter filter = new SecurityFilter(sessionRepository, currentTime);

    final Cookie cookie = new Cookie("sessionId", "sessionId");
    final Cookie[] cookies = new Cookie[]{cookie};

    context.checking(new Expectations() {{
      oneOf(request).getRequestURI();
      will(returnValue("/login"));

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findSessionById(cookie.getValue());
      will(returnValue(Optional.absent()));

      oneOf(filterChain).doFilter(request, response);
    }});

    filter.doFilter(request, response, filterChain);
  }

  @Test
  public void alreadyLogin() throws Exception {
    final SecurityFilter filter = new SecurityFilter(sessionRepository, currentTime);
    final TimeConverter converter = new TimeConverter();
    long timeForSessionLife = converter.convertStringToLong("00:06:0000");

    final Cookie cookie = new Cookie("sessionId", "sessionId");
    final Cookie[] cookies = new Cookie[]{cookie};
    final Session session = new Session(cookie.getValue(), "user@domain.com", timeForSessionLife);

    context.checking(new Expectations() {{
      oneOf(request).getRequestURI();
      will(returnValue("/login"));

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findSessionById(cookie.getValue());
      will(returnValue(Optional.of(session)));

      oneOf(currentTime).getCurrentTime();
      will(returnValue(converter.convertStringToLong("00:00:0000")));

      oneOf(response).sendRedirect("/home");

      oneOf(filterChain).doFilter(request, response);
    }});

    filter.doFilter(request, response, filterChain);
  }

  @Test
  public void getSecurityResourceNoSession() throws Exception {
    SecurityFilter filter = new SecurityFilter(sessionRepository, currentTime);
    final Cookie[] cookies = new Cookie[]{};

    context.checking(new Expectations() {{
      oneOf(request).getRequestURI();
      will(returnValue("/account"));

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findSessionById("");
      will(returnValue(Optional.absent()));

      oneOf(response).sendRedirect("/login");
    }});

    filter.doFilter(request, response, filterChain);
  }

  @Test
  public void removeTimeOutSessions() throws Exception {
    SecurityFilter filter = new SecurityFilter(sessionRepository, currentTime);

    final TimeConverter converter = new TimeConverter();
    long expirationTime = converter.convertStringToLong("00:06:0000");

    final Cookie cookie = new Cookie("sessionId", "sessionId");
    final Cookie[] cookies = new Cookie[]{cookie};
    final Session session = new Session("sessionId", "user@domain.com", expirationTime);

    context.checking(new Expectations() {{
      oneOf(request).getRequestURI();
      will(returnValue("/home"));

      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findSessionById("sessionId");
      will(returnValue(Optional.of(session)));

      oneOf(currentTime).getCurrentTime();
      will(returnValue(converter.convertStringToLong("00:12:0000")));

      oneOf(sessionRepository).remove(session.sessionId);

      oneOf(filterChain).doFilter(request, response);
    }});

    filter.doFilter(request, response, filterChain);
  }
}

