package com.clouway.adapter.http;

import com.clouway.core.FundsRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 13.06.16.
 */
public class TransactionHistoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  FundsRepository fundsRepository;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Test
  public void firstPage() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransactionHistory transactionHistory = new TransactionHistory(fundsRepository);

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

      oneOf(request).getParameter("offset");
      will(returnValue(null));

      oneOf(fundsRepository).getHistory(21, 0);
    }});
    transactionHistory.doGet(request, response);
  }

  @Test
  public void secondPage() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransactionHistory transactionHistory = new TransactionHistory(fundsRepository);
    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

      oneOf(request).getParameter("offset");
      will(returnValue(null));

      oneOf(fundsRepository).getHistory(21, 0);

      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

      oneOf(request).getParameter("offset");
      will(returnValue("20"));

      oneOf(request).getParameter("offset");
      will(returnValue("20"));

      oneOf(fundsRepository).getHistory(21, 20);
    }});
    transactionHistory.doGet(request, response);
    transactionHistory.doGet(request, response);
  }
}
