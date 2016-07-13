package com.clouway.bank.http;

import com.clouway.bank.adapter.http.AccountPageServlet;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.SessionIdFinder;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
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

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountPageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private SessionRepository sessionRepository;

  @Mock
  private AccountRepository accountRepository;

  private AccountPageServlet accountPageServlet;

  private ByteArrayOutputStream out;
  private PrintWriter writer;

  private Account account = new Account("user@domain.com", 22.00);
  long time = 123123123;
  private Session session = new Session("sessionId", "user@domain.com", time);
  private SessionIdFinder sessionIdFinder = new SessionIdFinder("sessionId");

  @Before
  public void setUp() throws Exception {
    accountPageServlet = new AccountPageServlet(sessionRepository, accountRepository, sessionIdFinder);
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
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(request).getParameter("errorMessage");
      will(returnValue(null));

      oneOf(sessionRepository).findUserEmailBySid(session.sessionId);
      will(returnValue(account.email));

      oneOf(accountRepository).findByEmail(account.email);
      will(returnValue(account));

      oneOf(response).getWriter();
      will(returnValue(writer));
    }});

    accountPageServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString(" <title>Account</title>"));
  }

  @Test
  public void loadPageWithErrorMessage() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(request).getParameter("errorMessage");
      will(returnValue("Error"));

      oneOf(response).getWriter();
      will(returnValue(writer));
    }});
    accountPageServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString("Error"));
  }
}
