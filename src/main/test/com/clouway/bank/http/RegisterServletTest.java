package com.clouway.bank.http;

import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
import com.clouway.bank.adapter.http.RegisterServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class RegisterServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private UserRepository repository = context.mock(UserRepository.class);
  private Validator validator = context.mock(Validator.class);

  private ByteArrayOutputStream out = new ByteArrayOutputStream();
  private PrintWriter writer = new PrintWriter(out);

  private Map<String, String> parameters = new HashMap<>();
  private FakeHttpRequest request = new FakeHttpRequest(parameters);

  @Before
  public void setUp() throws Exception {
    parameters.put("name", "Lilia");
    parameters.put("email", "lilia@abv.bg");
    parameters.put("password", "1234567");
    parameters.put("confirm", "1234567");
  }

  @After
  public void tearDown() throws Exception {
    out.close();
    writer.close();
  }

  @Test
  public void register() throws Exception {
    final RegisterServlet register = new RegisterServlet(repository, validator);

    final User user = getUser(request);

    context.checking(new Expectations() {{
      oneOf(validator).validate(user);
      will(returnValue(""));

      oneOf(repository).findByEmail("lilia@abv.bg");
      will(returnValue(null));

      oneOf(repository).register(user);

      oneOf(response).sendRedirect("/login");
    }});
    register.doPost(request, response);
  }

  @Test
  public void registerInvalidUser() throws Exception {
    RegisterServlet register = new RegisterServlet(repository, validator);

    final User user = getUser(request);

    context.checking(new Expectations() {{
      oneOf(validator).validate(user);
      will(returnValue("Error"));

      oneOf(response).sendRedirect("/register?errorMessage=Error");
    }});

    register.doPost(request, response);
  }

  @Test
  public void registerTwice() throws Exception {
    RegisterServlet register = new RegisterServlet(repository, validator);

    final User user1 = getUser(request);
    final User user2 = getUser(request);

    context.checking(new Expectations() {{
      oneOf(validator).validate(user1);
      will(returnValue(""));

      oneOf(repository).findByEmail("lilia@abv.bg");
      will(returnValue(null));

      oneOf(repository).register(user1);

      oneOf(response).sendRedirect("/login");

      oneOf(validator).validate(user2);
      will(returnValue("Error"));

      oneOf(response).sendRedirect("/register?errorMessage=Error");
    }});

    register.doPost(request, response);
    register.doPost(request, response);
  }

  private User getUser(FakeHttpRequest request) {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    return new User(name, email, password);
  }
}