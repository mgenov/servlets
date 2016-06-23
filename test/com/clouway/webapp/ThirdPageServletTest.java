package com.clouway.webapp;

import com.clouway.webapp.pages.ThirdPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ThirdPageServletTest {
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

  @After
  public void tearDown() throws Exception {
    writer.close();
    out.close();
  }

  @Test
  public void happyPath() throws Exception {
    ThirdPageServlet thirdPage = new ThirdPageServlet();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));
    }});

    thirdPage.doGet(request, response);
    String actual = out.toString();

    assertThat(actual, containsString("<h1>Third page</h1>"));
  }
}
