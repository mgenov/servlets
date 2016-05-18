package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by clouway on 18.05.16.
 */
public class DispatcherTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;


  @Test
  public void happyPath() throws Exception {
    Dispatcher dispatcher = new Dispatcher();
    context.checking(new Expectations() {{
      oneOf(request).getParameter("servletName");
      will(returnValue("first"));

      oneOf(request).setAttribute("servletName", "first");
      oneOf(request).getRequestDispatcher("/display");

    }});
    dispatcher.doGet(request, response);
  }
}
