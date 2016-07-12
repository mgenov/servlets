package com.clouway.bank.http;

import com.clouway.bank.adapter.http.HomePageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LogoutPageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);

  private ByteArrayOutputStream out;
  private PrintWriter writer;

  @Before
  public void setUp() throws Exception {
    out = new ByteArrayOutputStream();
    writer = new PrintWriter(out);
  }

  @Test
  public void loadResource() throws Exception {
    HomePageServlet homePageServlet = new HomePageServlet();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));
    }});

    homePageServlet.doGet(request, response);

    String page = out.toString();

    assertThat(page, containsString(" <title>Home</title>"));
  }
}
