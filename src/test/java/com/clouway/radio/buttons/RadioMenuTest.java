package com.clouway.radio.buttons;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RadioMenuTest {
  private RadioMenu menu;
  private ByteArrayOutputStream outputStream;
  private PrintWriter printWriter;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() {
    menu = new RadioMenu();
    outputStream = new ByteArrayOutputStream();
    printWriter = new PrintWriter(outputStream);
  }

  @After
  public void tearDown() throws IOException {
    printWriter.close();
    outputStream.close();
  }

  @Test
  public void redirectToLink() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue("first"));

      oneOf(response).sendRedirect("first");
    }});

    menu.doGet(request, response);
  }

  @Test
  public void redirectToAnotherLink() throws Exception {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue("second"));

      oneOf(response).sendRedirect("second");
    }});

    menu.doGet(request, response);
  }

  @Test
  public void showMenu() throws IOException, ServletException {
    context.checking(new Expectations() {{
      oneOf(request).getParameter("page");
      will(returnValue(null));

      oneOf(response).setContentType("text/html");

      oneOf(response).getWriter();
      will(returnValue(printWriter));
    }});

    menu.doGet(request, response);

    printWriter.flush();
    String result = outputStream.toString();

    assertThat(result, containsString("<form method='get'>\n<label>First</label><input type='radio' name='page' value='first'><br>\n" +
            "<label>Second</label><input type='radio' name='page' value='second'><br>\n" +
            "<label>Third</label><input type='radio' name='page' value='third'><br>\n" +
            "<input type='submit' value='submit'>\n</form>"));
  }
}