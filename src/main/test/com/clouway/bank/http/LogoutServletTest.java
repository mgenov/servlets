package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LogoutServlet;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.SessionIdFinder;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LogoutServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private SessionRepository sessionRepository = context.mock(SessionRepository.class);

  private final SessionIdFinder sessionIdFinder = new SessionIdFinder("sessionId");

  @Test
  public void logout() throws Exception {
    LogoutServlet logoutServlet = new LogoutServlet(sessionIdFinder, sessionRepository);

    final TimeConverter converter = new TimeConverter();
    long expirationTime = converter.convertStringToLong("00:12:0000");

    final Cookie cookie = new Cookie("sessionId", "sessionId");
    final Session userSession = new Session(cookie.getValue(), "user@domain.com", expirationTime);

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{cookie}));

      oneOf(sessionRepository).remove(userSession.sessionId);

      oneOf(response).sendRedirect("/login");
    }});
    logoutServlet.doGet(request, response);
  }

  @Test
  public void logoutNoLoginUser() throws Exception {
    LogoutServlet logoutServlet = new LogoutServlet(sessionIdFinder, sessionRepository);

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{}));
    }});
    logoutServlet.doGet(request, response);
  }
}
