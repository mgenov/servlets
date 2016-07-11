package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;
import com.clouway.bank.core.SessionRepository;
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

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class HomePageTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  Template template;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  SessionRepository sessionRepository;
  @Mock
  BankCalendar calendar;
  private HomePage homePage;

  @Before
  public void setUp() {
    homePage = new HomePage(template, sessionRepository, calendar);
  }

  @Test
  public void displayingOnlineUsersCount() throws ServletException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    context.checking(new Expectations() {{
      oneOf(calendar).getCurrentTime();
      will(returnValue(10034523L));

      oneOf(sessionRepository).countActive(10034523L);
      will(returnValue(100));

      oneOf(template).put("message", "100 online users");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");

      oneOf(template).evaluate();
      will(returnValue("100 online users"));
    }});

    homePage.doGet(request, response);
  }

  @Test
  public void displayingProblemCountingUsers() throws ServletException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(baos);
    context.checking(new Expectations() {{
      oneOf(calendar).getCurrentTime();
      will(returnValue(10034523L));

      oneOf(sessionRepository).countActive(10034523L);
      will(returnValue(null));

      oneOf(template).put("message", "We have a problem with counting the online users.");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");

      oneOf(template).evaluate();
      will(returnValue("We have a problem with counting the online users."));
    }});

    homePage.doGet(request, response);
  }
}
