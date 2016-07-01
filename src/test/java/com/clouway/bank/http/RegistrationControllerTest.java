package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
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
  private RegistrationController register;

  @Before
  public void setUp() {
    register = new RegistrationController(userRepository, accountRepository);
  }

  @Test
  public void registerUser() throws ServletException, IOException {
    context.checking(new Expectations() {{

      oneOf(request).getParameter("username");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("123456"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(userRepository).register(new User("Ivan", "123456"), "123456");

      oneOf(accountRepository).createAccount("Ivan");

      oneOf(response).sendRedirect("register?state=has-success&registerMessage=success");
    }});

    register.init();
    register.doPost(request, response);
  }

  @Test
  public void invalidUsername() throws ServletException, IOException {
    String exceptionMessage = "username too short, needs to be at least 5 characters";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue("Ivan"));

      oneOf(request).getParameter("password");
      will(returnValue("123456"));

      oneOf(request).getParameter("confirmPassword");
      will(returnValue("123456"));

      oneOf(userRepository).register(new User("Ivan", "123456"), "123456");
      will(throwException(new ValidationException("username too short, needs to be at least 5 characters")));

      oneOf(response).sendRedirect("register?state=has-error&registerMessage=" + exceptionMessage);
    }});

    register.init();

    register.doPost(request, response);
  }
}
