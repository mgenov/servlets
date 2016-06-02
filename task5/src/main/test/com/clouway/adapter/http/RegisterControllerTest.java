package com.clouway.adapter.http;

import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.Validator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public class RegisterControllerTest {
  private RegisterController registerController;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  UserRepository userRepo;

  @Mock
  Validator dataValidator;

  @Before
  public void setUp() {
    registerController = new RegisterController(userRepo, dataValidator);
  }

  @Test
  public void happyPath() throws Exception {
    final String email = "admin@clouwaybank.com";
    final String password = "admin123456";
    final String username = "GlobalAdmin";
    final User user = new User(username, password, email);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue(username));
      oneOf(request).getParameter("password");
      will(returnValue(password));
      oneOf(request).getParameter("email");
      will(returnValue(email));

      oneOf(dataValidator).isValid(username, password, email);
      will(returnValue(true));

      oneOf(userRepo).findByEmail(email);
      will(returnValue(null));

      oneOf(userRepo).register(user);

      oneOf(response).sendRedirect("/login?errorMsg=Registration successfull!");
    }});

    registerController.doPost(request, response);
  }

  @Test
  public void insertInvalidData() throws Exception {
    final String email = "adminclouwaybank.com";
    final String password = "admin123456";
    final String username = "GlobalAdmin";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue(username));
      oneOf(request).getParameter("password");
      will(returnValue(password));
      oneOf(request).getParameter("email");
      will(returnValue(email));

      oneOf(dataValidator).isValid(username, password, email);
      will(returnValue(false));

      oneOf(response).sendRedirect("/register?errorMsg=Input data is not in a valid format! Username and password should be between 6-16 characters and can contain only letters and digits.");
    }});

    registerController.doPost(request, response);
  }

  @Test
  public void registerAlreadyExistingUser() throws Exception {
    final String email = "admin@clouwaybank.com";
    final String password = "admin123456";
    final String username = "GlobalAdmin";
    final User user = new User(username, password, email);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue(username));
      oneOf(request).getParameter("password");
      will(returnValue(password));
      oneOf(request).getParameter("email");
      will(returnValue(email));

      oneOf(dataValidator).isValid(username, password, email);
      will(returnValue(true));

      oneOf(userRepo).findByEmail(email);
      will(returnValue(user));

      oneOf(response).sendRedirect("/register?errorMsg=Username with such an email already exist!");
    }});
    registerController.doPost(request, response);
  }
}
