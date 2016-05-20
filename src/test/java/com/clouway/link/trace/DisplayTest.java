package com.clouway.link.trace;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DisplayTest {
  private Display display;
  private ByteArrayOutputStream outputStream;
  private PrintWriter printWriter;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() {
    display = new Display();
    outputStream = new ByteArrayOutputStream();
    printWriter = new PrintWriter(outputStream);
  }

  @After
  public void tearDown() throws IOException {
    outputStream.close();
    printWriter.close();
  }

  @Test
  public void tracesRequestOrigin() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getHeader("referer");
      will(returnValue("firstPage"));

      oneOf(response).setContentType("text/html");

      oneOf(response).getWriter();
      will(returnValue(printWriter));
    }});

    display.doGet(request, response);
    printWriter.flush();

    String result = outputStream.toString();
    assertThat(result, containsString("<h1>firstPage</h1>"));
  }
}