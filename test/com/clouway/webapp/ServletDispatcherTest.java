package com.clouway.webapp;

import com.clouway.webapp.pages.ServletDispatcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServletDispatcherTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private RequestDispatcher dispatcher = context.mock(RequestDispatcher.class);

  @Test
  public void happyPath() throws Exception {
    final ServletDispatcher servletDispatcher = new ServletDispatcher();

    context.checking(new Expectations() {{
      oneOf(request).getRequestDispatcher("display");
      will(returnValue(dispatcher));

      oneOf(dispatcher).forward(request, response);
    }});

    servletDispatcher.doGet(request, response);
  }
}
