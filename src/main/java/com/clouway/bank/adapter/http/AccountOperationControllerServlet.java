package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.TransactionRepository;
import com.clouway.bank.core.Validator;
import com.clouway.bank.utils.SessionIdFinder;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountOperationControllerServlet extends HttpServlet {
  private final Validator validator;
  private final AccountRepository accountRepository;
  private final SessionRepository sessionRepository;
  private final SessionIdFinder sessionIdFinder;
  private final TransactionRepository transactionRepository;

  public AccountOperationControllerServlet(Validator validator, AccountRepository accountRepository, SessionRepository sessionRepository, SessionIdFinder sessionIdFinder, TransactionRepository transactionRepository) {
    this.validator = validator;
    this.accountRepository = accountRepository;
    this.sessionRepository = sessionRepository;
    this.sessionIdFinder = sessionIdFinder;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String operation = req.getParameter("operation");
    String amount = req.getParameter("amount");

    String message = validator.validate(amount);

    if (!Strings.isNullOrEmpty(message)) {
      resp.sendRedirect("/account?errorMessage=" + message);
      return;
    }

    String sessionId = sessionIdFinder.findSid(req.getCookies());

    String userEmail = sessionRepository.findUserEmailBySid(sessionId);

    if (operation.equals("deposit")) {
      accountRepository.deposit(userEmail, Double.valueOf(amount));
      transactionRepository.updateHistory(userEmail, operation, Double.valueOf(amount));
      resp.sendRedirect("/account");
      return;
    }

    Account account = accountRepository.findByEmail(userEmail);

    Double currentBalance = account.getBalance();

    boolean isInsufficient = currentBalance - Double.valueOf(amount) < 0;

    if (operation.equals("withdraw") && (!isInsufficient)) {
      accountRepository.withdraw(userEmail, Double.valueOf(amount));
      transactionRepository.updateHistory(userEmail, operation, Double.valueOf(amount));
      resp.sendRedirect("/account");

    } else if (isInsufficient) {
      resp.sendRedirect("/account?errorMessage=Your balance is not enough");
    }
  }
}
