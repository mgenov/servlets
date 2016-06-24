package com.clouway;

import com.clouway.buttons.ThirdPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ThirdPageServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private HttpServletRequest request = context.mock(HttpServletRequest.class);
  private HttpServletResponse response = context.mock(HttpServletResponse.class);
  private HttpSession session = context.mock(HttpSession.class);

  private PrintWriter writer;
  private ByteArrayOutputStream out;

  @Before
  public void setUp() throws Exception {
    out = new ByteArrayOutputStream();
    writer = new PrintWriter(out);
  }

  @After
  public void tearDown() throws Exception {
    writer.close();
    out.close();
  }

  @Test
  public void happyPath() throws Exception {
    ThirdPageServlet thirdPage = new ThirdPageServlet();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getSession(true);
      will(returnValue(session));

      oneOf(session).isNew();
      will(returnValue(true));
    }});

    thirdPage.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, containsString("<div>WELCOME!!!</div><p>This is third page</p>"));
  }

  @Test
  public void multipleVisits() throws Exception {
    ThirdPageServlet thirdPage = new ThirdPageServlet();

    context.checking(new Expectations() {{
      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(request).getSession(true);
      will(returnValue(session));

      oneOf(session).isNew();
      will(returnValue(false));
    }});

    thirdPage.doGet(request, response);

    String actual = out.toString();

    assertThat(actual, not(containsString("<div>WELCOME!!!</div>")));
  }
}
