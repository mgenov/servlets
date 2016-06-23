package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.utility.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DepositServletTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  AccountRepository repository;
  @Mock
  Template template;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  private DepositServlet depositServlet;

  @Before
  public void setUp() {
    depositServlet = new DepositServlet(repository, template);
  }

  @Test
  public void depositFunds() throws ServletException, IOException, ValidationException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(out);

    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("12.5"));

      oneOf(repository).deposit("John", "12.5");
      will(returnValue(12.5d));

      oneOf(template).put("errorMessage", "");

      oneOf(template).put("username", "John");

      oneOf(template).put("balance", "12.5");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");

      oneOf(template).evaluate();
      will(returnValue("OK"));
    }});

    depositServlet.init();
    depositServlet.doPost(request, response);

    writer.flush();
    assertThat(out.toString(), is(equalTo("OK")));
  }

  @Test
  public void invalidDeposit() throws IOException, ValidationException, ServletException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(out);

    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("username");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("12.5e"));

      oneOf(repository).deposit("John", "12.5e");
      will(throwException(new ValidationException("invalid amount")));

      oneOf(template).put("errorMessage", "invalid amount<br>");

      oneOf(response).getWriter();
      will(returnValue(writer));

      oneOf(response).setContentType("text/html");

      oneOf(template).evaluate();
      will(returnValue("OK"));
    }});

    depositServlet.init();
    depositServlet.doPost(request, response);

    writer.flush();
    assertThat(out.toString(), is(equalTo("OK")));
  }

  private ExpectationBuilder getInitializationExpectations() throws IOException {
    return new Expectations() {{
      //initializing servlet
      oneOf(template).setTemplate(readFile("web/WEB-INF/pages/Deposit.html"));
      oneOf(template).put("errorMessage", "");
      oneOf(template).put("username", "no user yet");
      oneOf(template).put("balance", "not available");
    }};
  }

  private String readFile(String filePath) throws IOException {
    File file = new File(filePath);
    URL url = null;
    url = file.toURI().toURL();
    InputStream in = url.openStream();

    return IOUtils.toString(in);
  }
}
