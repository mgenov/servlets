package com.clouway.http.servlets;

import com.clouway.FakeHttpServletRequest;
import com.clouway.FakeHttpServletResponse;
import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.ServletResponseWriter;
import com.clouway.core.Template;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class LoginPageServletTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private FakeHttpServletRequest request = new FakeHttpServletRequest();
  private FakeHttpServletResponse response = new FakeHttpServletResponse();
  private ByteArrayOutputStream stream;
  private LoginPageServlet servlet;
  private PrintWriter writer;

  private AccountRepository repo = context.mock(AccountRepository.class);
  private Template template = context.mock(Template.class);
  private ServletResponseWriter servletResponseWriter = context.mock(ServletResponseWriter.class);

  @Before
  public void setUp() throws Exception {
    servlet = new LoginPageServlet(repo, template, servletResponseWriter);
    stream = new ByteArrayOutputStream();
    writer = new PrintWriter(stream);
  }

  @Test
  public void happyPath() throws Exception {
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(template).evaluate();
      will(returnValue("page"));

      oneOf(servletResponseWriter).writeResponse(response, "page");
    }});

    servlet.doGet(request, response);
  }

  @Test
  public void login() throws Exception {
    request.setParameter("name", "John");
    request.setParameter("password", "pwd");
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(repo).getByName("John");
      will(returnValue(Optional.of(new Account("John", "pwd", 0))));

    }});

    servlet.doPost(request, response);
    assertThat(response.getRedirect(), is("/account"));
  }

  @Test
  public void wrongUsername() throws Exception {
    request.setParameter("name", "John");
    request.setParameter("password", "pwd");
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(repo).getByName("John");
      will(returnValue(Optional.empty()));
      oneOf(template).put("error", "Wrong username");
      oneOf(template).evaluate();
      will(returnValue("Wrong username"));

      oneOf(servletResponseWriter).writeResponse(response, "Wrong username");
    }});

    servlet.doPost(request, response);
  }

  @Test
  public void wrongPassword() throws Exception {
    request.setParameter("name", "John");
    request.setParameter("password", "pwd");
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(repo).getByName("John");
      will(returnValue(Optional.of(new Account("John", "wrong", 0))));
      oneOf(template).put("error", "Wrong password");
      oneOf(template).evaluate();
      will(returnValue("Wrong password"));

      oneOf(servletResponseWriter).writeResponse(response, "Wrong password");
    }});

    servlet.doPost(request, response);
  }
}