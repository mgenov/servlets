package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationController extends HttpServlet {
  private UserRepository userRepository;
  private AccountRepository accountRepository;


  public RegistrationController(UserRepository userRepository, AccountRepository accountRepository) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String state;
    String message;

    try {
      userRepository.register(new User(username, password), confirmPassword);
      state = "has-success";
      message = "success";

      accountRepository.createAccount(username);
    } catch (ValidationException e) {
      state = "has-error";
      message = e.getMessage();
    }
    response.sendRedirect("register?state=" + state + "&registerMessage=" + message);

  }
}
