package com.clouway.webapp;

import com.clouway.webapp.pages.ServletDisplay;
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
public class ServletDisplayTest {

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
    out.close();
    writer.close();
  }

  @Test
  public void happyPath() throws Exception {
    ServletDisplay display = new ServletDisplay();

    context.checking(new Expectations() {{
      oneOf(request).getHeader("Referer");
      will(returnValue("first"));

      oneOf(response).getWriter();
      will(returnValue(writer));
    }});

    display.doGet(request, response);

    String actual = out.toString();
    assertThat(actual, containsString("The name of the page that send request is: first"));
  }
}
