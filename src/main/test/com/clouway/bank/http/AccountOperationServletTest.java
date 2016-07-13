package com.clouway.bank.http;

import com.clouway.bank.adapter.http.AccountOperationControllerServlet;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.TransactionRepository;
import com.clouway.bank.core.Validator;
import com.clouway.bank.utils.SessionIdFinder;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountOperationServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Validator<String> validator;

  @Mock
  private SessionRepository sessionRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private Provider provider;

  @Mock
  private TransactionRepository transactionRepository;

  private AccountOperationControllerServlet accountOperationControllerServlet;

  private final long time = 123123123;
  private final Session session = new Session("sessionId", "krasimir@abv.bg", time);
  private final SessionIdFinder sessionIdFinder = new SessionIdFinder("sessionId");
  private final Account account = new Account("user@domain.com", 22.00);

  @Before
  public void setUp() throws Exception {
    accountOperationControllerServlet = new AccountOperationControllerServlet(validator, accountRepository, sessionRepository, sessionIdFinder, transactionRepository);
  }

  @Test
  public void deposit() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("operation");
      will(returnValue("deposit"));

      oneOf(request).getParameter("amount");
      will(returnValue(String.valueOf(22.00)));

      oneOf(validator).validate("22.0");
      will(returnValue(""));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid("sessionId");
      will(returnValue(session.sessionId));

      oneOf(accountRepository).deposit("sessionId", 22.00);

      oneOf(transactionRepository).updateHistory(session.sessionId, "deposit", 22.00);

      oneOf(response).sendRedirect("/account");
    }});

    accountOperationControllerServlet.doPost(request, response);
  }


  @Test
  public void depositWithInvalidAmount() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("operation");
      will(returnValue("deposit"));

      oneOf(request).getParameter("amount");
      will(returnValue(String.valueOf(22.00)));

      oneOf(validator).validate("22.0");
      will(returnValue("Error"));

      oneOf(response).sendRedirect("/account?errorMessage=Error");
    }});

    accountOperationControllerServlet.doPost(request, response);
  }

  @Test
  public void withdraw() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("operation");
      will(returnValue("withdraw"));

      oneOf(request).getParameter("amount");
      will(returnValue(String.valueOf(15.00)));

      oneOf(validator).validate("15.0");
      will(returnValue(""));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid("sessionId");
      will(returnValue(session.sessionId));

      oneOf(accountRepository).findByEmail(session.sessionId);
      will(returnValue(account));

      oneOf(accountRepository).withdraw("sessionId", 15.00);

      oneOf(transactionRepository).updateHistory(session.sessionId, "withdraw", 15.00);

      oneOf(response).sendRedirect("/account");
    }});

    accountOperationControllerServlet.doPost(request, response);
  }

  @Test
  public void withdrawWithInvalidAmount() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("operation");
      will(returnValue("withdraw"));

      oneOf(request).getParameter("amount");
      will(returnValue("2222.22.22"));

      oneOf(validator).validate("2222.22.22");
      will(returnValue("Error"));

      oneOf(response).sendRedirect("/account?errorMessage=Error");
    }});

    accountOperationControllerServlet.doPost(request, response);
  }

  @Test
  public void withdrawInsufficientAmount() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("operation");
      will(returnValue("withdraw"));

      oneOf(request).getParameter("amount");
      will(returnValue(String.valueOf(100.00)));

      oneOf(validator).validate("100.0");
      will(returnValue(""));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid(session.sessionId);
      will(returnValue(session.sessionId));

      oneOf(accountRepository).findByEmail(session.sessionId);
      will(returnValue(account));

      oneOf(response).sendRedirect("/account?errorMessage=Your balance is not enough");
    }});

    accountOperationControllerServlet.doPost(request, response);
  }
}