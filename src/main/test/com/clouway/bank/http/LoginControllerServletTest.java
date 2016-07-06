package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LoginControllerServlet;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
  private SessionRepository sessionRepository = context.mock(SessionRepository.class);


  @Test
  public void happyPath() throws Exception {
    LoginControllerServlet servlet = new LoginControllerServlet(repository, sessionRepository, validator);
    final User user = new User("Ivan", "ivan@abv.bg", "password");
    final Session userSession = new Session("sessionId", "ivan@abv.bg", getTime("12:12:12"));

    context.checking(new Expectations() {{
      oneOf(request).getParameter("email");
      will(returnValue(user.email));

      oneOf(request).getParameter("password");
      will(returnValue(user.password));

      oneOf(validator).validate(user.email, user.password);
      will(returnValue(""));

      oneOf(repository).findByEmail(user.email);
      will(returnValue(user));

//      oneOf(request).getSession();
//      will(returnValue(session));

      oneOf(request).getSession().getId();
      will(returnValue(userSession.sessionId));
    }});
    servlet.doPost(request, response);
  }

  private long getTime(String time) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:aa");

    return dateFormat.parse(time).getTime();
  }
}
