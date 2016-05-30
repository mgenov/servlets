package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import sun.awt.image.ImageWatched.Link;

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
  public void loadPage() throws Exception {
    LinkHitCounter linkhitcounter = new LinkHitCounter();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(outputStream)));
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(request).getParameter("link");
      will(returnValue(null));

    }});

    linkhitcounter.doGet(request, response);
  }

  @Test
  public void visitFirstLink() throws Exception {
    LinkHitCounter linkHitCounter = new LinkHitCounter();
    final Map<String, Integer> visitedPages = new HashMap<String, Integer>();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(outputStream)));
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(request).getParameter("link");
      will(returnValue("first"));
      oneOf(session).getAttribute("links");
      will(returnValue(visitedPages));
      oneOf(session).setAttribute("links", visitedPages);
    }});

    linkHitCounter.doGet(request, response);

    String expected = outputStream.toString();
    assertThat(expected, containsString("You have accessed this link 1 times."));

  }

  @Test
  public void visitSecondLinkTwice() throws Exception {
    LinkHitCounter linkHitCounter = new LinkHitCounter();
    final Map<String, Integer> visitedPages = new HashMap<String, Integer>();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(outputStream)));
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(request).getParameter("link");
      will(returnValue("second"));
      oneOf(session).getAttribute("links");
      will(returnValue(visitedPages));
      oneOf(session).setAttribute("links", visitedPages);

      oneOf(response).getWriter();
      will(returnValue(new PrintWriter(outputStream)));
      oneOf(request).getSession();
      will(returnValue(session));
      oneOf(request).getParameter("link");
      will(returnValue("second"));
      oneOf(session).getAttribute("links");
      will(returnValue(visitedPages));
      oneOf(session).setAttribute("links", visitedPages);
    }});

    linkHitCounter.doGet(request, response);
    linkHitCounter.doGet(request, response);

    String expected = outputStream.toString();
    assertThat(expected, containsString("You have accessed this link 2 times."));
  }
}
