package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LoginControllerServlet;
import com.clouway.bank.core.Generator;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.SessionTime;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
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
public class LoginControllerServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private UserRepository repository = context.mock(UserRepository.class);

  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private Validator<User> validator = context.mock(Validator.class);

  private SessionTime time = context.mock(SessionTime.class);
  private Generator generator = context.mock(Generator.class);

  @Test
  public void login() throws Exception {
    LoginControllerServlet servlet = new LoginControllerServlet(repository, sessionRepository, validator, time, generator);
    final TimeConverter timeConverter = new TimeConverter();
    long timeForSessionLife = timeConverter.convertStringToLong("12:12:0000");

    final User user = new User("Ivan", "ivan@abv.bg", "password");
    final Session userSession = new Session("sessionId", "ivan@abv.bg", timeForSessionLife);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue("ivan@abv.bg"));

      oneOf(request).getParameter(user.password);
      will(returnValue("password"));

      oneOf(validator).validate(user.email, user.password);
      will(returnValue(""));

      oneOf(repository).findByEmail(user.email);
      will(returnValue(user));

      oneOf(generator).generate();
      will(returnValue(userSession.sessionId));

      oneOf(time).getTimeOfLife();
      will(returnValue(userSession.sessionLifeTime));

      oneOf(sessionRepository).createSession(userSession);

      oneOf(response).addCookie(with(any(Cookie.class)));

      oneOf(response).sendRedirect("/home");
    }});

    servlet.doPost(request, response);
  }

  @Test
  public void loginNoRegisteredUser() throws Exception {
    LoginControllerServlet servlet = new LoginControllerServlet(repository, sessionRepository, validator, time, generator);
    final User user = new User("Maria", "m@avc.bg", "password");

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(user.email));

      oneOf(request).getParameter("password");
      will(returnValue(user.password));

      oneOf(validator).validate(user.email, user.password);
      will(returnValue(""));

      oneOf(repository).findByEmail(user.email);
      will(returnValue(null));

      oneOf(response).sendRedirect("/login?errorMessage=You should register first!");
    }});

    servlet.doPost(request, response);
  }

  @Test
  public void loginInvalidUser() throws Exception {
    LoginControllerServlet servlet = new LoginControllerServlet(repository, sessionRepository, validator, time, generator);
    final User user = new User("Maria", "email", "password");

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(user.email));

      oneOf(request).getParameter("password");
      will(returnValue(user.password));

      oneOf(validator).validate(user.email, user.password);
      will(returnValue("Error"));

      oneOf(response).sendRedirect("/login?errorMessage=Error");
    }});

    servlet.doPost(request, response);
  }
}
