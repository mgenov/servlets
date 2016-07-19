package com.clouway.bank.http;

import com.clouway.bank.core.AccountPager;
import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionProvider;
import com.clouway.utility.RowBuilder;
import com.clouway.utility.TableBuilder;
import com.clouway.utility.Template;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class AccountHistoryPageTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  Template template;
  @Mock
  TableBuilder tableBuilder;
  @Mock
  RowBuilder rowBuilder;
  @Mock
  AccountPager accountPager;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  SessionProvider sessionProvider;
  private AccountHistoryPage historyPage;

  @Before
  public void setUp() {
    historyPage = new AccountHistoryPage(template, tableBuilder, accountPager, sessionProvider);
  }

  @Test
  public void displayFirstPage() throws ServletException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    List<AccountRecord> records = new ArrayList<>();

    AccountRecord stanislavaDeposit = new AccountRecord(1L, "Stanislava", "deposit", 10.0D);
    records.add(stanislavaDeposit);

    Session session = new Session("1", "Stanislava", 10L);

    context.checking(new Expectations() {{
      oneOf(sessionProvider).get();
      will(returnValue(session));

      oneOf(request).getParameter("page");
      will(returnValue("1"));

      oneOf(template).put("previousState", "auto");

      oneOf(accountPager).requestPage("Stanislava", 0);
      will(returnValue(records));

      allowing(tableBuilder).aNewRow();

      oneOf(accountPager).getPreviousPageNumber();
      will(returnValue(0));

      oneOf(accountPager).getCurrentPageNumber();
      will(returnValue(0));

      oneOf(accountPager).getNextPageNumber();
      will(returnValue(1));

      oneOf(template).put("previousState", "none");

      oneOf(template).put("nextState", "auto");

      oneOf(template).put("previous", "1");

      oneOf(template).put("currentPage", "1");

      oneOf(template).put("next", "2");

      oneOf(template).put("userId", "Stanislava");

      oneOf(tableBuilder).build();
      will(returnValue("first page"));

      oneOf(template).put("table", "first page");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");
      oneOf(template).evaluate();
      will(returnValue("first page"));
    }});

    historyPage.doGet(request, response);
    writer.flush();

    assertThat(baos.toString(), is(equalTo("first page")));
  }

  @Test
  public void secondPage() throws IOException, ServletException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    List<AccountRecord> records = new ArrayList<>();

    AccountRecord stanislavaDeposit = new AccountRecord(1L, "Stanislava", "deposit", 10.0D);
    records.add(stanislavaDeposit);

    Session session = new Session("3", "Stanislava", 1034L);

    context.checking(new Expectations() {{
      oneOf(sessionProvider).get();
      will(returnValue(session));

      oneOf(request).getParameter("page");
      will(returnValue("2"));

      oneOf(template).put("previousState", "auto");

      oneOf(accountPager).requestPage("Stanislava", 1);
      will(returnValue(records));

      allowing(tableBuilder).aNewRow();

      oneOf(accountPager).getPreviousPageNumber();
      will(returnValue(0));

      oneOf(accountPager).getCurrentPageNumber();
      will(returnValue(0));

      oneOf(accountPager).getNextPageNumber();
      will(returnValue(1));

      oneOf(template).put("previousState", "none");

      oneOf(template).put("nextState", "auto");

      oneOf(template).put("previous", "1");

      oneOf(template).put("currentPage", "1");

      oneOf(template).put("next", "2");

      oneOf(template).put("userId", "Stanislava");

      oneOf(tableBuilder).build();
      will(returnValue("second page"));

      oneOf(template).put("table", "second page");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");
      oneOf(template).evaluate();
      will(returnValue("second page"));
    }});

    historyPage.doGet(request, response);
    writer.flush();

    assertThat(baos.toString(), is(equalTo("second page")));
  }

  @Test
  public void disableNext() throws IOException, ServletException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    List<AccountRecord> records = new ArrayList<>();

    AccountRecord stanislavaDeposit = new AccountRecord(1L, "Stanislava", "deposit", 10.0D);
    records.add(stanislavaDeposit);

    Session session = new Session("1", "Stanislava", 1033L);

    context.checking(new Expectations() {{
      oneOf(sessionProvider).get();
      will(returnValue(session));

      oneOf(request).getParameter("page");
      will(returnValue("2"));

      oneOf(template).put("previousState", "auto");

      oneOf(accountPager).requestPage("Stanislava", 1);
      will(returnValue(records));

      oneOf(template).put("nextState", "auto");


      allowing(tableBuilder).aNewRow();

      oneOf(accountPager).getPreviousPageNumber();
      will(returnValue(0));

      oneOf(accountPager).getCurrentPageNumber();
      will(returnValue(1));

      oneOf(accountPager).getNextPageNumber();
      will(returnValue(1));

      oneOf(template).put("nextState", "none");

      oneOf(template).put("previous", "1");

      oneOf(template).put("currentPage", "2");

      oneOf(template).put("next", "2");

      oneOf(template).put("userId", "Stanislava");

      oneOf(tableBuilder).build();
      will(returnValue("second page"));

      oneOf(template).put("table", "second page");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");
      oneOf(template).evaluate();
      will(returnValue("second page"));
    }});

    historyPage.doGet(request, response);
    writer.flush();

    assertThat(baos.toString(), is(equalTo("second page")));
  }
}