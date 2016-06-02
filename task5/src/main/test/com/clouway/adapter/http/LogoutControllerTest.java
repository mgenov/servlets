package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public class LogoutControllerTest {
  private LogoutController logoutController;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  SessionRepository sessionRepo;

  @Mock
  CookieFinder cookieFinder;

  @Before
  public void setUp() {
    logoutController = new LogoutController(sessionRepo, cookieFinder);
  }

  @Test
  public void happyPath() throws Exception {
    final Cookie[] cookies = new Cookie[]{};
    final Cookie cookie = new Cookie("sessionId", "3131-3344-6667-8843");

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));
      oneOf(sessionRepo).delete(cookie.getValue());

      oneOf(response).addCookie(cookie);
      oneOf(response).sendRedirect("/login");
    }});
    logoutController.doPost(request, response);
  }

  @Test
  public void cookieNotFound() throws Exception {
    final Cookie[] cookies = new Cookie[]{};

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(null));

      oneOf(response).sendRedirect("/login");
    }});
    logoutController.doPost(request, response);
  }
}
