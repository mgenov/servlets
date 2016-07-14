package com.clouway.bank.http;

import com.clouway.bank.adapter.http.HomePageServlet;
import com.clouway.bank.core.SessionRepository;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HomePageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private SessionRepository sessionRepository = context.mock(SessionRepository.class);
  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);

  private PrintWriter writer;
  private ByteArrayOutputStream out;

  @Before
  public void setUp() throws Exception {
    out = new ByteArrayOutputStream();
    writer = new PrintWriter(out);
  }

  @Test
  public void loadHomePageOneUserOnline() throws Exception {
    HomePageServlet homePageServlet = new HomePageServlet(sessionRepository);

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getParameter("page");
      will(returnValue("/account"));

      oneOf(response).sendRedirect("/account");

      oneOf(sessionRepository).getOnlineUsersCount();
      will(returnValue(1));
    }});

    homePageServlet.doGet(request, response);

    String pageContent = out.toString();

    assertThat(pageContent, containsString("Online users: 1"));
  }
}
