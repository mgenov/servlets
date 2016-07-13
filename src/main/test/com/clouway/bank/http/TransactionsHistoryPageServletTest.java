package com.clouway.bank.http;

import com.clouway.bank.adapter.http.TransactionHistoryPageServlet;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
import com.clouway.bank.utils.SessionIdFinder;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class TransactionsHistoryPageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private SessionRepository sessionRepository;

  private ByteArrayOutputStream out;
  private PrintWriter writer;

  private TransactionHistoryPageServlet servlet;
  private long dateAsLong = 18181818;
  private Transaction transaction;

  private final Double processingAmount = 10.00;

  @Before
  public void setUp() throws Exception {
    transaction = new Transaction(dateAsLong, "a@abv.bg", "deposit", processingAmount, 100.00);

    servlet = new TransactionHistoryPageServlet(transactionRepository, 4, sessionRepository, new SessionIdFinder("sessionId"));

    out = new ByteArrayOutputStream();
    writer = new PrintWriter(out);
  }

  @Test
  public void loadFirstPageByDefault() throws Exception {
    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getParameter("page");
      will(returnValue(null));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid("sessionId");
      will(returnValue("a@abv.bg"));

      oneOf(transactionRepository).getTransactions("a@abv.bg", 4, 0);
      will(returnValue(Lists.newArrayList(transaction)));

      oneOf(transactionRepository).getNumberOfRecords();
      will(returnValue(4));

    }});
    servlet.doGet(request, response);
  }

  @Test
  public void loadFirstPage() throws Exception {
    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getParameter("page");
      will(returnValue("1"));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid("sessionId");
      will(returnValue("a@abv.bg"));

      oneOf(transactionRepository).getTransactions(transaction.email, 4, 0);
      will(returnValue(Lists.newArrayList(transaction)));

      oneOf(transactionRepository).getNumberOfRecords();
      will(returnValue(4));

    }});
    servlet.doGet(request, response);
  }

  @Test
  public void loadSecondPage() throws Exception {
    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getParameter("page");
      will(returnValue("2"));

      oneOf(request).getCookies();
      will(returnValue(new Cookie[]{new Cookie("sessionId", "sessionId")}));

      oneOf(sessionRepository).findUserEmailBySid("sessionId");
      will(returnValue("a@abv.bg"));

      oneOf(transactionRepository).getTransactions(transaction.email, 4, 4);
      will(returnValue(Lists.newArrayList(transaction)));

      oneOf(transactionRepository).getNumberOfRecords();
      will(returnValue(10));

    }});

    servlet.doGet(request, response);
  }
}
