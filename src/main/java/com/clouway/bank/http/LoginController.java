package com.clouway.bank.http;

import com.clouway.bank.core.BankCalendar;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.UserValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@WebServlet(name = "LoginController")
public class LoginController extends HttpServlet {
  private final UserRepository userRepository;
  private final UserValidator userValidator;
  private SessionRepository sessionRepository;
  private BankCalendar calendar;

  public LoginController(SessionRepository sessionRepository, UserRepository userRepository, UserValidator userValidator, BankCalendar calendar) {

    this.sessionRepository = sessionRepository;
    this.userRepository = userRepository;
    this.userValidator = userValidator;
    this.calendar = calendar;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("userId");
    String password = request.getParameter("password");


    String userValidationMessage = userValidator.validate(new User(username, password));
    if (!"".equals(userValidationMessage)) {
      response.sendRedirect("login?message=" + userValidationMessage);
      return;
    }

    try {
      User user = userRepository.getUserById(username);
      String message = userValidator.passwordsMatch(user.password, password);

      if (!"".equals(message)) {
        response.sendRedirect("login?message=" + message);
        return;
      }


      String sessionId = UUID.randomUUID().toString();
      sessionRepository.create(new Session(sessionId, username, calendar.getExpirationTime()));
      Cookie cookie = new Cookie("sessionId", sessionId);
      response.addCookie(cookie);
      response.sendRedirect("");
    } catch (NullPointerException e) {
      response.sendRedirect("login?message=Sorry could not find such user!");
      return;
    }
  }
}
