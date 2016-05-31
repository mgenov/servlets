package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by clouway on 17.05.16.
 */
public class YahooServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  HttpSession session;

  @Test
  public void firstTimePageLoad() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Yahoo yahoo = new Yahoo();
    final String atr = "Yahoo";

    context.checking(new Expectations() {{
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute(atr);
      will(returnValue(null));
      oneOf(session).setAttribute(atr, "visited");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
    }});

    yahoo.doGet(request, response);

    String expected = out.toString();

    assertThat(expected, containsString("<!DOCTYPE html>"));
    assertThat(expected, containsString("<html>"));
    assertThat(expected, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected, containsString("<title>Yahoo post service</title></head><body>"));
    assertThat(expected, containsString("<h1>Welcome! You visited Yahoo post service for the first time!</h1>"));
    assertThat(expected, containsString("</body></html>"));
  }

  @Test
  public void secondOrMoreTimePageLoad() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Yahoo yahoo = new Yahoo();
    final String atr = "Yahoo";

    context.checking(new Expectations() {{
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute(atr);
      will(returnValue(null));
      oneOf(session).setAttribute(atr, "visited");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute(atr);
      will(returnValue("visited"));
      oneOf(session).setAttribute(atr, "visited");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

    }});

    yahoo.doGet(request, response);
    String expected = out.toString();
    assertThat(expected, containsString("<h1>Welcome! You visited Yahoo post service for the first time!</h1>"));
    yahoo.doGet(request, response);

    String expected1 = out.toString();

    assertThat(expected1, containsString("<!DOCTYPE html>"));
    assertThat(expected1, containsString("<html>"));
    assertThat(expected1, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected1, containsString("<title>Yahoo post service</title></head><body>"));
    assertThat(expected1, containsString("</body></html>"));
  }
}
