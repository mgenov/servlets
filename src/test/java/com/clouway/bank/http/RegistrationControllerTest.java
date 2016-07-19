package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.UserValidator;
import com.clouway.bank.core.ValidationException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationControllerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  UserRepository userRepository;
  @Mock
  AccountRepository accountRepository;
  @Mock
  UserValidator validator;
  private RegistrationController register;

  @Before
  public void setUp() {
    register = new RegistrationController(userRepository, accountRepository, validator);
  }

  @Test
  public void registerUser() throws ServletException, IOException {
    User ivan = new User("Ivan", "123456");

    context.checking(new Expectations() {{

      oneOf(request).getParameter("userId");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("123456"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(validator).validate(ivan);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("123456", "123456");

      oneOf(userRepository).register(ivan);

      oneOf(accountRepository).createAccount("Ivan");

      oneOf(response).sendRedirect("login?message=successful registration");
    }});

    register.init();
    register.doPost(request, response);
  }

  @Test
  public void invalidUsername() throws ServletException, IOException {
    String exceptionMessage = "userId too short, needs to be at least 5 characters";
    User ivan = new User("Ivan", "123456");

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("123456"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(validator).validate(ivan);
      will(returnValue(exceptionMessage));

      oneOf(validator).passwordsMatch("123456", "123456");
      will(returnValue(""));

      oneOf(response).sendRedirect("register?state=has-error&registerMessage=" + exceptionMessage);
    }});

    register.init();

    register.doPost(request, response);
  }

  @Test
  public void passwordsNotMatching() throws IOException, ServletException {
    User ivan = new User("Ivan", "1234567");

    context.checking(new Expectations() {{

      oneOf(request).getParameter("userId");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("1234567"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(validator).validate(ivan);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("1234567", "123456");
      will(returnValue("passwords dont match"));

      oneOf(response).sendRedirect("register?state=has-error&registerMessage=passwords dont match");
    }});

    register.init();
    register.doPost(request, response);
  }

  @Test
  public void usernameTaken() throws IOException, ServletException {
    User ivan = new User("Ivan", "123456");

    context.checking(new Expectations() {{

      oneOf(request).getParameter("userId");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("123456"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(validator).validate(ivan);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("123456", "123456");

      oneOf(userRepository).register(ivan);
      will(throwException(new ValidationException("userId is taken")));

      oneOf(response).sendRedirect("register?state=has-error&registerMessage=userId is taken");
    }});

    register.init();
    register.doPost(request, response);
  }
}
