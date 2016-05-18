package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by clouway on 16.05.16.
 */
public class LinkHitCoutnerTest {
  private ByteArrayOutputStream outputStream;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  HttpSession session;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Before
  public void setUp() {
    outputStream = new ByteArrayOutputStream();
  }

  @After
  public void cleanUp() throws IOException {
    outputStream.close();
  }


  @Test
  public void happyPath() throws Exception {
    LinkHitCounter linkhitcounter = new LinkHitCounter();
    final Map<String, Integer> counter = new HashMap<String, Integer>();
    counter.put("first", 0);
    counter.put("second", 0);
    counter.put("third", 0);

    context.checking(new Expectations() {
      {
        oneOf(request).getSession();
        will(returnValue(session));
        oneOf(request).getParameter("link");
        will(returnValue(null));
        oneOf(session).getAttribute("links");
        will(returnValue(null));
        oneOf(session).setAttribute("links", counter);
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));

      }
    });

    linkhitcounter.doGet(request, response);

    String expected = outputStream.toString();

    assertThat(expected, containsString("<!DOCTYPE html>"));
    assertThat(expected, containsString("<html>"));
    assertThat(expected, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected, containsString("<title>Session Test Servlet</title></head><body>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=first\">ABV</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 0 times.</h2>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=second\">GMAIL</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 0 times.</h2>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=third\">YAHOO</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 0 times.</h2>"));
    assertThat(expected, containsString("</body></html>"));
  }

  @Test
  public void clickOnlyFirstLink() throws Exception {
    LinkHitCounter linkhitcounter = new LinkHitCounter();
    final Map<String, Integer> counter = new HashMap<String, Integer>();
    counter.put("first", 0);
    counter.put("second", 0);
    counter.put("third", 0);

    context.checking(new Expectations() {
      {
        oneOf(request).getSession();
        will(returnValue(session));
        oneOf(request).getParameter("link");
        will(returnValue(null));
        oneOf(session).getAttribute("links");
        will(returnValue(null));
        oneOf(session).setAttribute("links", counter);
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));

        oneOf(request).getSession();
        will(returnValue(session));
        oneOf(request).getParameter("link");
        will(returnValue("first"));
        oneOf(session).getAttribute("links");
        will(returnValue(counter));
        oneOf(session).setAttribute("links", counter);
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));
      }
    });

    linkhitcounter.doGet(request, response);
    linkhitcounter.doGet(request, response);

    String expected = outputStream.toString();

    assertThat(expected, containsString("<!DOCTYPE html>"));
    assertThat(expected, containsString("<html>"));
    assertThat(expected, containsString("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"));
    assertThat(expected, containsString("<title>Session Test Servlet</title></head><body>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=first\">ABV</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 1 times.</h2>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=second\">GMAIL</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 0 times.</h2>"));
    assertThat(expected, containsString("<p><a  href=\"linkhitcounter?link=third\">YAHOO</a>"));
    assertThat(expected, containsString("<h2 style=\"color:blue;\">You have accessed this link 0 times.</h2>"));
    assertThat(expected, containsString("</body></html>"));
  }
}
