package com.clouway.bank.adapter.http;

import com.clouway.bank.core.IdGenerator;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.CurrentTime;
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
  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  private final Validator<User> validator;
  private final CurrentTime time;
  private final IdGenerator generator;

  public LoginControllerServlet(UserRepository userRepository, SessionRepository sessionRepository, Validator<User> validator, CurrentTime time, IdGenerator generator) {
    this.userRepository = userRepository;
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

    User user = userRepository.findByEmail(email);
    if (user == null || !user.password.equals(password)) {
      resp.sendRedirect("/login?errorMessage=You should register first!");

    } else {
      String sessionId = generator.generateId();

      Session session = new Session(sessionId, email, time.expirationTime());
      sessionRepository.createSession(session);

      Cookie cookie = new Cookie("sessionId", sessionId);

      cookie.setMaxAge((int) time.expirationTime());
      resp.addCookie(cookie);

      resp.sendRedirect("/home");
    }
  }
}
