package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LoginControllerServlet;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginControllerServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private UserRepository repository = context.mock(UserRepository.class);
  private Validator<User> validator = context.mock(Validator.class);

  @Test
  public void happyPath() throws Exception {
    LoginControllerServlet servlet = new LoginControllerServlet(repository, validator);

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
    }});
  }
}
