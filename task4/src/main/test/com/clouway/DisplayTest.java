package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by clouway on 18.05.16.
 */
public class DisplayTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;


  @Test
  public void happyPath() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Display display = new Display();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
      oneOf(request).getParameter("servletName");
      will(returnValue("first"));
      oneOf(request).getAttribute("errorMsg");
      will(returnValue(null));
    }});

    display.doGet(request, response);

    String expected = out.toString();

    assertThat(expected, containsString("first"));
  }

  @Test
  public void requestFromUntrustedPage() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Display display = new Display();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
      oneOf(request).getParameter("servletName");
      will(returnValue("blabla"));
      oneOf(request).getAttribute("errorMsg");
      will(returnValue("Request from unknown servlet!"));
    }});

    display.doGet(request, response);

    String expected = out.toString();

    assertThat(expected, containsString("Request from unknown servlet!"));

  }
}
