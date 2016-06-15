package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.FundsRepository;
import com.clouway.core.SessionRepository;
import com.clouway.core.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 08.06.16.
 */
@WebServlet(name = "BankOperationHandler")
public class BankOperationHandler extends HttpServlet {
  private FundsRepository fundsRepository;
  private Validator validator;
  private CookieFinder cookieFinder;
  private SessionRepository sessionRepository;


  public BankOperationHandler(FundsRepository fundsRepository, Validator validator, CookieFinder cookieFinder, SessionRepository sessionRepository) {
    this.fundsRepository = fundsRepository;
    this.validator = validator;
    this.cookieFinder = cookieFinder;
    this.sessionRepository = sessionRepository;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    Cookie cookie = cookieFinder.find(cookies);
    String sessionId = cookie.getValue();
    String email = sessionRepository.getCurrentUserEmail(sessionId);
    String operation = request.getParameter("operation");
    String amount = request.getParameter("amount");

    if (amount.equalsIgnoreCase("")) {
      response.sendRedirect("/useraccount?message=No amount entered!");
      return;
    }

    if (!validator.isValid(amount)) {
      response.sendRedirect("/useraccount?message=Amount not valid. Example for a valid amount: 123.23 or 100.00");
      return;
    }

    Double amountAsDouble = Double.valueOf(amount);

    if (operation.equalsIgnoreCase("Deposit")) {
      fundsRepository.deposit(amountAsDouble, email);
      fundsRepository.updateHistory(email, operation, amountAsDouble);
      response.sendRedirect("/useraccount?message=Deposit successful!");
      return;
    }

    boolean success = fundsRepository.withdraw(amountAsDouble, email);

    if (success) {
      fundsRepository.updateHistory(email, operation, amountAsDouble);
      response.sendRedirect("/useraccount?message=Withdraw successful!");
      return;
    }

    response.sendRedirect("/useraccount?message=Not enough funds!");
  }
}
