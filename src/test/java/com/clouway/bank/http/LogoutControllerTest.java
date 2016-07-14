package com.clouway.bank.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionProvider;
import com.clouway.bank.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class LogoutControllerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  SessionRepository sessionRepository;
  @Mock
  SessionProvider sessionProvider;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  private LogoutController logoutController;

  @Before
  public void setUp() {
    logoutController = new LogoutController(sessionRepository, sessionProvider);
  }


  @Test
  public void happyPath() throws ServletException, IOException {
    Session session = new Session("1", "Krasimir", 123L);
    context.checking(new Expectations() {{
      oneOf(sessionProvider).get();
      will(returnValue(session));

      oneOf(sessionRepository).remove(session.id);

      oneOf(response).addCookie(with(any(Cookie.class)));

      oneOf(response).sendRedirect("/login");
    }});
    logoutController.doGet(request, response);
  }
}
