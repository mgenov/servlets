package com.clouway.adapter.http;

import com.clouway.core.RandomGenerator;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.UserRepository;
import com.clouway.core.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 27.05.16.
 */
@WebServlet(name = "LoginController")
public class LoginController extends HttpServlet {
  private UserRepository userRepository;
  private SessionRepository sessionRepository;
  private Validator userValidator;
  private RandomGenerator randomGenerator;

  public LoginController(UserRepository userRepository, SessionRepository sessionRepository, Validator userValidator, RandomGenerator randomGenerator) {
    this.userRepository = userRepository;
    this.sessionRepository = sessionRepository;
    this.userValidator = userValidator;
    this.randomGenerator = randomGenerator;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    boolean valid = userValidator.isValid(email, password);
    if (!valid) {
      response.sendRedirect("/login?errorMsg=Email or password invalid format!");
      return;
    }

    boolean isAuthenticated = userRepository.authenticate(email, password);
    if (isAuthenticated) {
      String uuid = randomGenerator.generateUUID();
      Session session = new Session(email, uuid);
      sessionRepository.create(session);
      Cookie cookie = new Cookie("sessionId", uuid);
      cookie.setMaxAge(300);
      response.addCookie(cookie);
      response.sendRedirect("/useraccount");
      return;
    }

    response.sendRedirect("/login?errorMsg=Wrong email or password!");
  }
}
