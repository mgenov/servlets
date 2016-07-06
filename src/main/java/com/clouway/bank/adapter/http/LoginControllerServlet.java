package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginControllerServlet extends HttpServlet {
  private final UserRepository repository;
  private final SessionRepository sessionRepository;
  private final Validator<User> validator;

  public LoginControllerServlet(UserRepository repository, SessionRepository sessionRepository, Validator<User> validator) {
    this.repository = repository;
    this.sessionRepository = sessionRepository;
    this.validator = validator;
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    String message = validator.validate(email, password);
    Map<String, String> users = repository.findAll(email);

    if (!(message.equals(""))) {
      resp.sendRedirect("/login?errorMessage=" + message);
    }

    if (repository.findByEmail(email) == null || !users.containsValue(password)) {
      resp.sendRedirect("/login?errorMessage=You should register first!");

    } else {

      HttpSession session = req.getSession(true);

      Session userSession = new Session(UUID.randomUUID().toString(), email, session.getCreationTime());

      session.setAttribute("id", userSession.sessionId);
      Cookie cookie = new Cookie("email", email);
      resp.addCookie(cookie);

      sessionRepository.save(userSession);
      resp.sendRedirect("/home");
    }
  }
}
