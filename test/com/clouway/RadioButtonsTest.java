package com.clouway;

import com.clouway.buttons.RadioButtonsServlet;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class RadioButtonsTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);

  private PrintWriter writer;
  private ByteArrayOutputStream out;

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
    RadioButtonsServlet radioButton = new RadioButtonsServlet();
    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getParameter("page");
      will(returnValue("first"));

      oneOf(response).sendRedirect("first");

    }});
    radioButton.doGet(request, response);
    writer.flush();
    
    String actual = out.toString();

    assertThat(actual, containsString("<input type=\"radio\" name=\"page\" value=\"second\">"));
  }
}
