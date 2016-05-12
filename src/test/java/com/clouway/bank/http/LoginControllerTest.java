package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.UserValidator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
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
public class LoginControllerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  UserRepository userRepository;
  @Mock
  UserValidator userValidator;
  @Mock
  SessionRepository sessionRepository;
  @Mock
  BankCalendar calendar;

  @Test
  public void loginUser() throws ServletException, IOException {
    LoginController loginController = new LoginController(sessionRepository, userRepository, userValidator, calendar);
    User johna = new User("Johna", "123456");
    String enteredPassword = "123456";
    Long expTime = 1000L;
    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("Johna"));

      oneOf(request).getParameter("password");
      will(returnValue(enteredPassword));

      oneOf(userValidator).validate(johna);
      will(returnValue(""));

      oneOf(userRepository).getUserById("Johna");
      will(returnValue(johna));

      oneOf(userValidator).passwordsMatch(johna.password, enteredPassword);
      will(returnValue(""));

      oneOf(calendar).getExpirationTime();
      will(returnValue(expTime));

      oneOf(sessionRepository).create(with(any(Session.class)));

      oneOf(response).addCookie(with(any(Cookie.class)));

      oneOf(response).sendRedirect("");
    }});

    loginController.doPost(request, response);
  }

  @Test
  public void invalidUser() throws ServletException, IOException {
    LoginController loginController = new LoginController(sessionRepository, userRepository, userValidator, calendar);
    User johna = new User("Johna", "123456");
    String enteredPassword = "123456";
    Session session = new Session("1", "Johna", 1000L);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("Johna"));

      oneOf(request).getParameter("password");
      will(returnValue(enteredPassword));

      oneOf(userValidator).validate(johna);
      will(returnValue("userId should be ..."));

      oneOf(response).sendRedirect("login?message=userId should be ...");

    }});

    loginController.doPost(request, response);
  }

  @Test
  public void passwordsDoNotMatch() throws IOException, ServletException {
    LoginController loginController = new LoginController(sessionRepository, userRepository, userValidator, calendar);
    User johna = new User("Johna", "qwerty");
    User johnaRep = new User("Johna", "123456");
    String enteredPassword = "qwerty";
    Session session = new Session("1", "Johna", 1000L);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("Johna"));

      oneOf(request).getParameter("password");
      will(returnValue(enteredPassword));

      oneOf(userValidator).validate(johna);
      will(returnValue(""));

      oneOf(userRepository).getUserById("Johna");
      will(returnValue(johnaRep));

      oneOf(userValidator).passwordsMatch("123456", "qwerty");
      will(returnValue("passwords don't match"));

      oneOf(response).sendRedirect("login?message=passwords don't match");

    }});

    loginController.doPost(request, response);
  }

  @Test
  public void userNotFound() throws IOException, ServletException {
    LoginController loginController = new LoginController(sessionRepository, userRepository, userValidator, calendar);
    User johna = new User("Johna", "123456");
    String enteredPassword = "123456";
    Long expTime = 1000L;
    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("Johna"));

      oneOf(request).getParameter("password");
      will(returnValue(enteredPassword));

      oneOf(userValidator).validate(johna);
      will(returnValue(""));

      oneOf(userRepository).getUserById("Johna");
      will(returnValue(null));

      oneOf(response).sendRedirect("login?message=Sorry could not find such user!");
    }});

    loginController.doPost(request, response);
  }

}