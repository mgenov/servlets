package com.clouway.bank.http;

import com.clouway.bank.adapter.http.HomeControllerServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HomeControllerServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);

  @Test
  public void happyPath() throws Exception {
    HomeControllerServlet controllerServlet = new HomeControllerServlet();
    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue("/LOGIN"));

      oneOf(response).sendRedirect("/LOGIN");
    }});
    controllerServlet.doPost(request, response);

  }
}
