package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Generator;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.SessionTime;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginControllerServlet extends HttpServlet {
  private final UserRepository repository;
  private final SessionRepository sessionRepository;
  private final Validator<User> validator;
  private final SessionTime time;
  private final Generator generator;

  public LoginControllerServlet(UserRepository repository, SessionRepository sessionRepository, Validator<User> validator, SessionTime time, Generator generator) {
    this.repository = repository;
    this.sessionRepository = sessionRepository;
    this.validator = validator;
    this.time = time;
    this.generator = generator;
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    String message = validator.validate(email, password);

    if (!(Strings.isNullOrEmpty(message))) {
      resp.sendRedirect("/login?errorMessage=" + message);
      return;
    }

    User user = repository.findByEmail(email);
    if (user == null || !user.password.equals(password)) {
      resp.sendRedirect("/login?errorMessage=You should register first!");

    } else {
      String id = generator.generate();

      Session session = new Session(id, email, time.getTimeOfLife());
      sessionRepository.createSession(session);

      Cookie cookie = new Cookie("id", id);

      cookie.setMaxAge(100);
      resp.addCookie(cookie);

      resp.sendRedirect("/home");
    }
  }
}
