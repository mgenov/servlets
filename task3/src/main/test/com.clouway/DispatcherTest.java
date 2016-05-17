package com.clouway;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by clouway on 17.05.16.
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
    final String page = "http://localhost:8080/abv.html";
    Dispatcher dispatecher = new Dispatcher();
    context.checking(new Expectations() {{
      oneOf(request).getParameter("button");
      will(returnValue(page));
      oneOf(response).setContentType("text/html;charset=UTF-8");
      oneOf(response).sendRedirect(page);
    }});

    dispatecher.doGet(request, response);

  }
}
