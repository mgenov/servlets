package com.clouway.bank.adapter.http;

import com.clouway.bank.core.*;
import com.clouway.bank.core.Account;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountControllerServlet extends HttpServlet {
  private final Validator validator;
  private final AccountRepository accountRepository;
  private final SessionRepository sessionRepository;

  public AccountControllerServlet(Validator validator, AccountRepository accountRepository, SessionRepository sessionRepository) {
    this.validator = validator;
    this.accountRepository = accountRepository;
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Cookie[] cookies = req.getCookies();
    Cookie cookie = find(cookies);

    String operation = req.getParameter("operation");
    String cash = req.getParameter("cash");
    String email;
    String message = validator.validate(cash);
    if (!(message.equals(""))) {
      resp.sendRedirect("/account?errorMessage=" + message);

    } else {
      if (cookie != null) {
        email = sessionRepository.findEmailById(cookie.getValue());
        Account account = new Account(email, Double.valueOf(cash));
        if (accountRepository.findByEmail(email) == null) {
          accountRepository.createAccount(account);
        } else if (operation.equals("deposit")) {
          accountRepository.deposit(email, Double.valueOf(cash));
          resp.sendRedirect("account");
        } else if (operation.equals("withdraw")) {
          Double currentBalance = accountRepository.getBalance(email);
          if (currentBalance - Double.valueOf(cash) > 0)
            accountRepository.withdraw(email, Double.valueOf(cash));
          resp.sendRedirect("account");
        }
      }
    }
  }

  private void getOperation(AccountRepository repository, String operation,Account account,String cash) {
    if (operation.equals("deposit")) {
      repository.deposit(account.email,Double.valueOf(cash));

    }else if (operation.equals("withdraw")){

    }
  }

  private Cookie find(Cookie[] cookies) {
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("id")) {
        return cookie;
      }
    }
    return null;
  }
}
