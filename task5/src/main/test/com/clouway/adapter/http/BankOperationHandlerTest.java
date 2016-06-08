package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.FundsRepository;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.UserRepository;
import com.clouway.core.Validator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 08.06.16.
 */
public class BankOperationHandlerTest {
  private BankOperationHandler bankOperationHandler;
  private String amount;
  private String email;
  private Double amountInDouble;
  private Cookie[] cookies;
  private Cookie cookie;


  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  Validator validator;

  @Mock
  FundsRepository fundsRepository;

  @Mock
  CookieFinder cookieFinder;

  @Mock
  SessionRepository sessionRepository;

  @Before
  public void setUp() {
    bankOperationHandler = new BankOperationHandler(fundsRepository, validator, cookieFinder, sessionRepository);
    amount = "200.00";
    email = "admin@abv.bg";
    amountInDouble = 200.00D;
    cookies = new Cookie[]{};
    cookie = new Cookie("sessionId", "3131-3344-6667-8843");
  }

  @Test
  public void depositMoney() throws Exception {

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).getCurrentUserEmail(cookie.getValue());
      will(returnValue(email));

      oneOf(request).getParameter("operation");
      will(returnValue("Deposit"));
      oneOf(request).getParameter("amount");
      will(returnValue(amount));

      oneOf(validator).isValid(amount);
      will(returnValue(true));

      oneOf(fundsRepository).deposit(amountInDouble, email);
      oneOf(response).sendRedirect("/useraccount?message=Deposit successful!");
    }});

    bankOperationHandler.doPost(request, response);
  }

  @Test
  public void withdrawMoney() throws Exception {

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).getCurrentUserEmail(cookie.getValue());
      will(returnValue(email));

      oneOf(request).getParameter("operation");
      will(returnValue("Withdraw"));
      oneOf(request).getParameter("amount");
      will(returnValue(amount));

      oneOf(validator).isValid(amount);
      will(returnValue(true));

      oneOf(fundsRepository).withdraw(amountInDouble, email);
      will(returnValue(true));
      oneOf(response).sendRedirect("/useraccount?message=Withdraw successful!");
    }});

    bankOperationHandler.doPost(request, response);
  }

  @Test
  public void invalidAmount() throws Exception {

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).getCurrentUserEmail(cookie.getValue());
      will(returnValue(email));

      oneOf(request).getParameter("operation");
      will(returnValue("Withdraw"));
      oneOf(request).getParameter("amount");
      will(returnValue(amount));

      oneOf(validator).isValid(amount);
      will(returnValue(false));

      oneOf(response).sendRedirect("/useraccount?message=Amount not valid. Example for a valid amount: 123.23 or 100.00");
    }});

    bankOperationHandler.doPost(request, response);
  }

  @Test
  public void withdrawWithNotEnoughMoney() throws Exception {

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).getCurrentUserEmail(cookie.getValue());
      will(returnValue(email));

      oneOf(request).getParameter("operation");
      will(returnValue("Withdraw"));
      oneOf(request).getParameter("amount");
      will(returnValue(amount));

      oneOf(validator).isValid(amount);
      will(returnValue(true));

      oneOf(fundsRepository).withdraw(amountInDouble, email);
      will(returnValue(false));
      oneOf(response).sendRedirect("/useraccount?message=Not enough funds!");
    }});

    bankOperationHandler.doPost(request, response);
  }

  @Test
  public void depositWithEmptySum() throws Exception {
    final String amount = "";

    context.checking(new Expectations() {{
      oneOf(request).getCookies();
      will(returnValue(cookies));

      oneOf(cookieFinder).find(cookies);
      will(returnValue(cookie));

      oneOf(sessionRepository).getCurrentUserEmail(cookie.getValue());
      will(returnValue(email));

      oneOf(request).getParameter("operation");
      will(returnValue("Deposit"));
      oneOf(request).getParameter("amount");
      will(returnValue(amount));

      oneOf(response).sendRedirect("/useraccount?message=No amount entered!");
    }});

    bankOperationHandler.doPost(request, response);
  }
}
