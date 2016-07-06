package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.UserValidator;

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
  private UserValidator validator;


  public RegistrationController(UserRepository userRepository, AccountRepository accountRepository, UserValidator validator) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;

    this.validator = validator;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String state;
    String message;

    User user = new User(username, password);

    String validationMessage = validator.validate(user);
    validationMessage += validator.passwordsMatch(user.password, confirmPassword);

    if (!"".equals(validationMessage)) {
      state = "has-error";
      message = validationMessage;
    } else {
      userRepository.register(user);
      state = "has-success";
      message = "success";

      accountRepository.createAccount(username);
    }


    response.sendRedirect("register?state=" + state + "&registerMessage=" + message);

  }
}
