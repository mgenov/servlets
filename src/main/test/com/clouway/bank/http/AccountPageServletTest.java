package com.clouway.bank.http;

import com.clouway.bank.adapter.http.AccountPageServlet;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountPageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private AccountRepository accountRepository = context.mock(AccountRepository.class);

  private ByteArrayOutputStream out;
  private PrintWriter writer;

  @Before
  public void setUp() throws Exception {
    out = new ByteArrayOutputStream();
    writer = new PrintWriter(out);
  }

  @After
  public void tearDown() throws Exception {
    out.close();
    writer.close();
  }

  @Test
  public void loadPage() throws Exception {
    AccountPageServlet accountPageServlet = new AccountPageServlet(sessionRepository, accountRepository);
    final Account account = new Account("user@domain.com", 22.00);
    final Session session = new Session("sessionId", "user@domain.com", getTime("12:12:0000"));
    final Cookie cookie = new Cookie("id", session.sessionId);
    final Cookie[] cookies = new Cookie[]{cookie};

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findEmailById(session.sessionId);
      will(returnValue(session.email));

      oneOf(request).getParameter("errorMessage");
      will(returnValue(""));

      oneOf(accountRepository).getBalance(account.email);
      will(returnValue(22.00));

      oneOf(response).getWriter();
      will(returnValue(writer));
    }});

    accountPageServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString(" <title>Account</title>"));
  }

  @Test
  public void loadPageWithValidationError() throws Exception {
    AccountPageServlet accountPageServlet = new AccountPageServlet(sessionRepository, accountRepository);
    final Account account = new Account("user@domain.com", 22.00);
    final Session session = new Session("sessionId", "user@domain.com", getTime("12:12:0000"));
    final Cookie cookie = new Cookie("id", session.sessionId);
    final Cookie[] cookies = new Cookie[]{cookie};

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(sessionRepository).findEmailById(cookie.getValue());
      will(returnValue("user@domain.com"));

      oneOf(request).getParameter("errorMessage");
      will(returnValue("Error"));

      oneOf(accountRepository).getBalance(account.email);
      will(returnValue(22.00));

      oneOf(response).getWriter();
      will(returnValue(writer));
    }});
    accountPageServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString("Error"));
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
