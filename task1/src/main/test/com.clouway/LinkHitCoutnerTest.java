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

    context.checking(new Expectations() {
      {
        oneOf(request).getSession();
        will(returnValue(session));
        ;
        oneOf(request).getParameter("link");
        will(returnValue(null));
        oneOf(session).getAttribute(null);
        will(returnValue(null));
        oneOf(session).setAttribute("first", 0);
        oneOf(session).setAttribute("second", 0);
        oneOf(session).setAttribute("third", 0);
        oneOf(response).setContentType("text/html;charset=UTF-8");
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));
        oneOf(session).getAttribute("first");
        will(returnValue(0));
        oneOf(session).getAttribute("second");
        will(returnValue(0));
        oneOf(session).getAttribute("third");
        will(returnValue(0));
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
    context.checking(new Expectations() {
      {
        oneOf(request).getSession();
        will(returnValue(session));
        ;
        oneOf(request).getParameter("link");
        will(returnValue(null));
        oneOf(session).getAttribute(null);
        will(returnValue(null));
        oneOf(session).setAttribute("first", 0);
        oneOf(session).setAttribute("second", 0);
        oneOf(session).setAttribute("third", 0);
        oneOf(response).setContentType("text/html;charset=UTF-8");
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));
        oneOf(session).getAttribute("first");
        will(returnValue(0));
        oneOf(session).getAttribute("second");
        will(returnValue(0));
        oneOf(session).getAttribute("third");
        will(returnValue(0));

        oneOf(request).getSession();
        will(returnValue(session));
        oneOf(request).getParameter("link");
        will(returnValue("first"));
        oneOf(response).setContentType("text/html;charset=UTF-8");
        oneOf(response).getWriter();
        will(returnValue(new PrintWriter(outputStream)));
        oneOf(session).getAttribute("first");
        will(returnValue(0));
        oneOf(session).setAttribute("first", 1);
        oneOf(session).getAttribute("first");
        will(returnValue(1));
        oneOf(session).getAttribute("second");
        will(returnValue(0));
        oneOf(session).getAttribute("third");
        will(returnValue(0));
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
