package com.clouway.bank.adapter.http;

import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginControllerServlet extends HttpServlet {
  private final UserRepository repository;
  private final Validator<User> validator;

  public LoginControllerServlet(UserRepository repository, Validator<User> validator) {
    this.repository = repository;
    this.validator = validator;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    String message = validator.validate(email, password);
    if (!(message.equals(""))) {
      resp.sendRedirect("/login?errorMessage=" + message);
    }
    if (repository.findByEmail(email) == null) {
      resp.sendRedirect("/login?errorMessage=You should register first!");
    } else {
      resp.sendRedirect("/home");
    }
  }
}
