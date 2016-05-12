package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.SessionProvider;
import com.clouway.bank.core.TransactionValidator;
import com.clouway.bank.core.ValidationException;
import com.clouway.utility.Template;
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
import java.io.IOException;

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
  @Mock
  TransactionValidator validator;
  @Mock
  SessionProvider sessionProvider;

  private DepositServlet depositServlet;

  @Before
  public void setUp() {
    depositServlet = new DepositServlet(repository, template, validator, sessionProvider);
  }

  @Test
  public void depositFunds() throws ServletException, IOException, ValidationException {
    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("12.5"));

      oneOf(validator).validateAmount("12.5");
      will(returnValue(""));

      oneOf(repository).deposit(new Amount("John", 12.5));
      will(returnValue(12.5));

      oneOf(response).sendRedirect("deposit?message=Successful deposit: 12.5");
    }});

    depositServlet.init();
    depositServlet.doPost(request, response);
  }

  private ExpectationBuilder getInitializationExpectations() throws IOException {
    return new Expectations() {{
      //initializing servlet
      oneOf(template).loadFromFile("web/WEB-INF/pages/Deposit.html");
    }};
  }
}
