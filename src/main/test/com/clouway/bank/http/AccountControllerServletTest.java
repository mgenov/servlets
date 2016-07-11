package com.clouway.bank.http;

import com.clouway.bank.adapter.http.AccountControllerServlet;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentAccountRepository;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Validator;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountControllerServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private Validator validator = context.mock(Validator.class);
  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private Provider provider = context.mock(Provider.class);

  @Test
  public void happyPath() throws Exception {
    AccountControllerServlet accountControllerServlet = new AccountControllerServlet(validator, new PersistentAccountRepository(provider), sessionRepository);
    final Session session = new Session("sessionId", "krasimir@abv.bg", getTime("12:12:0000"));
    final Cookie cookie = new Cookie("id", session.sessionId);
    final Cookie[] cookies = new Cookie[]{cookie};

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(request).getParameter("errorMessage");
      will(returnValue(""));

      oneOf(validator).validate("cash");
      will(returnValue(""));

      oneOf(sessionRepository).findEmailById(session.sessionId);
      will(returnValue("krasimir@abv.bg"));

      oneOf(request).getParameter("operation");
      will(returnValue("deposit"));

      oneOf(request).getParameter("cash");
      will(returnValue(String.valueOf(22.00)));

      oneOf(validator).validate("22.0");
      will(returnValue(""));
    }});
    accountControllerServlet.doPost(request, response);
  }

  private long getTime(String timeAsString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ssss");

    Date date = null;
    try {
      date = simpleDateFormat.parse(timeAsString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
