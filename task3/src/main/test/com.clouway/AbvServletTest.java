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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by clouway on 17.05.16.
 */
public class AbvServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  HttpSession session;

  @Test
  public void firstTimeLoad() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Abv abv = new Abv();

    context.checking(new Expectations(){{
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute("abvVisited");
      will(returnValue(null));
      oneOf(session).setAttribute("abvVisited","true");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));
    }});

    abv.doGet(request,response);

    String expected=out.toString();

    assertThat(expected, containsString("<!DOCTYPE html>"));
    assertThat(expected, containsString("<html>"));
    assertThat(expected, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected, containsString("<title>Abv post service</title></head><body>"));
    assertThat(expected, containsString("<h1>Welcome! You visited Abv post service for the first time!</h1>"));
    assertThat(expected, containsString("</body></html>"));
  }

  @Test
  public void secondOrMoreTimePageLoad() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Abv abv = new Abv();

    context.checking(new Expectations(){{
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute("abvVisited");
      will(returnValue(null));
      oneOf(session).setAttribute("abvVisited","true");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(session).getAttribute("abvVisited");
      will(returnValue("true"));
      oneOf(session).setAttribute("abvVisited","true");
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(out)));

    }});

    abv.doGet(request,response);
    String expected=out.toString();
    assertThat(expected, containsString("<h1>Welcome! You visited Abv post service for the first time!</h1>"));

    abv.doGet(request,response);
    String expected2=out.toString();

    assertThat(expected2, containsString("<!DOCTYPE html>"));
    assertThat(expected2, containsString("<html>"));
    assertThat(expected2, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected2, containsString("<title>Abv post service</title></head><body>"));
    assertThat(expected2, containsString("<h1></h1>"));
    assertThat(expected2, containsString("</body></html>"));


  }
}
