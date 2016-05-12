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
public class WithdrawServletTest {

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

  private WithdrawServlet withdrawServlet;

  @Before
  public void setUp() {
    withdrawServlet = new WithdrawServlet(repository, template, validator, sessionProvider);
  }

  @Test
  public void withdrawFunds() throws ServletException, IOException, ValidationException {
    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("0.5"));

      oneOf(validator).validateAmount("0.5");
      will(returnValue(""));

      oneOf(repository).withdraw(new Amount("John", 0.5));
      will(returnValue(12.5d));

      oneOf(response).sendRedirect("withdraw?message=Successful withdraw: 0.5");
    }});

    withdrawServlet.init();
    withdrawServlet.doPost(request, response);
  }

  @Test
  public void invalidWithdraw() throws IOException, ValidationException, ServletException {
    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("12.5e"));

      oneOf(validator).validateAmount("12.5e");
      will(returnValue("unacceptable number format"));

      oneOf(response).sendRedirect("withdraw?message=unacceptable number format");
    }});

    withdrawServlet.init();
    withdrawServlet.doPost(request, response);
  }

  private ExpectationBuilder getInitializationExpectations() throws IOException {
    return new Expectations() {{
      //initializing servlet
      oneOf(template).loadFromFile("web/WEB-INF/pages/Withdraw.html");
      oneOf(template).put("message", "");
    }};
  }

  @Test
  public void insufficientBalance() throws IOException, ServletException {
    context.checking(getInitializationExpectations());

    context.checking(new Expectations() {{
      oneOf(request).getParameter("userId");
      will(returnValue("John"));

      oneOf(request).getParameter("amount");
      will(returnValue("0.5"));

      oneOf(validator).validateAmount("0.5");
      will(returnValue(""));

      oneOf(repository).withdraw(new Amount("John", 0.5));
      will(throwException(new ValidationException("Insufficient balance")));

      oneOf(response).sendRedirect("withdraw?message=Insufficient balance");
    }});

    withdrawServlet.init();
    withdrawServlet.doPost(request, response);
  }

}
