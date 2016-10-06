package com.clouway.servlets.radio.servlets;

import com.clouway.servlets.radio.Core.Template;
import com.clouway.servlets.radio.FakeHttpServletRequest;
import com.clouway.servlets.radio.FakeHttpServletResponse;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class PagesServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private Template template = context.mock(Template.class);

  private FakeHttpServletResponse response;
  private FakeHttpServletRequest request;
  private ByteArrayOutputStream stream;
  private Map<String, String> pages;
  private PagesServlet servlet;
  private PrintWriter writer;

  @Before
  public void setUp() throws Exception {
    response = new FakeHttpServletResponse();
    request = new FakeHttpServletRequest();
    pages = new LinkedHashMap<String, String>() {{
      put("page1", "Hello!");
    }};
    servlet = new PagesServlet(template, pages);
    stream = new ByteArrayOutputStream();
    writer = new PrintWriter(stream);
  }

  @Test
  public void happyPath() throws Exception {
    request.setParameter("link", "page1");
    response.setWriter(writer);

    String expected = pages.get("page1");

    context.checking(new Expectations() {{
      oneOf(template).put("greeting", "Welcome to page1.");
      oneOf(template).setTemplateValue(expected);
      oneOf(template).evaluate();
      will(returnValue(expected));
    }});

    servlet.doGet(request, response);

    String actual = stream.toString();
    assertThat(response.getStatus(), is(200));
    assertThat(actual, is(expected));
  }
}