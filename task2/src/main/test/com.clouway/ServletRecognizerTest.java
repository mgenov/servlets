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
 * Created by clouway on 17.05.16.
 */
public class ServletRecognizerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Test
  public void requestFromTrustedPage() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    RecognizerServlet recognizerServlet = new RecognizerServlet();

    final String page = "Abv";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue(page));
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
    }});

    recognizerServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString("Request from Abv page"));
  }

  @Test
  public void requestFromUntrustedPage() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    RecognizerServlet recognizerServlet = new RecognizerServlet();

    final String page = "CNN";

    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue(page));
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
    }});

    recognizerServlet.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString("Request from untrusted page!"));
  }
}
