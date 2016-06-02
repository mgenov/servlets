package com.clouway.adapter.http;

import com.clouway.core.RandomGenerator;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.Validator;
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
public class LoginControllerTest {
  private LoginController loginController;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  UserRepository userRepo;

  @Mock
  SessionRepository sessionRepo;

  @Mock
  Validator dataValidator;

  @Mock
  RandomGenerator randomGenerator;


  @Before
  public void setUp() {
    loginController = new LoginController(userRepo, sessionRepo, dataValidator, randomGenerator);
  }

  @Test
  public void happyPath() throws Exception {
    final String email = "kristiyan@gmail.com";
    final String password = "123456";
    final String uuid = "1451421-512411-51421-45124151";
    final Session session = new Session(email, uuid);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(email));
      oneOf(request).getParameter("password");
      will(returnValue(password));

      oneOf(dataValidator).isValid(email, password);
      will(returnValue(true));

      oneOf(userRepo).authenticate(email, password);
      will(returnValue(true));

      oneOf(randomGenerator).generateUUID();
      will(returnValue(uuid));

      oneOf(sessionRepo).create(session);

      oneOf(response).addCookie(with(any(Cookie.class)));
      oneOf(response).sendRedirect("/useraccount");
    }});

    loginController.doPost(request, response);
  }

  @Test
  public void loginParametersNotValid() throws Exception {
    final String email = "John.abv.bg";
    final String password = "223344";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(email));
      oneOf(request).getParameter("password");
      will(returnValue(password));

      oneOf(dataValidator).isValid(email, password);
      will(returnValue(false));

      oneOf(response).sendRedirect("/login?errorMsg=Email or password invalid format!");
    }});
    loginController.doPost(request, response);
  }

  @Test
  public void userNotAuthenticated() throws Exception {
    final String email = "John@abv.bg";
    final String password = "223344";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(email));
      oneOf(request).getParameter("password");
      will(returnValue(password));

      oneOf(dataValidator).isValid(email, password);
      will(returnValue(true));

      oneOf(userRepo).authenticate(email, password);
      will(returnValue(false));

      oneOf(response).sendRedirect("/login?errorMsg=Wrong email or password!");
    }});
    loginController.doPost(request, response);
  }
}
