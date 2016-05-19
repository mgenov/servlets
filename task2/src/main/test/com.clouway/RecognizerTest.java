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
public class RecognizerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Test
  public void happyPath() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    RecognizerServlet recognizerServlet = new RecognizerServlet();
    final String expected="http://localhost:8080/pages/abv.html";
    context.checking(new Expectations() {{
      oneOf(request).getHeader("Referer");
      will(returnValue(expected));
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
    }});

    recognizerServlet.doGet(request,response);

    String actual = out.toString();

    assertThat(actual, containsString(expected));

  }
}
