package com.clouway.adapter.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.core.UserRepository;
import com.clouway.core.UserValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 27.05.16.
 */
@WebServlet(name = "LoginController")
public class LoginController extends HttpServlet {
  private UserRepository userRepository;
  private SessionRepository sessionRepository;

  public LoginController(UserRepository userRepository, SessionRepository sessionRepository) {
    this.userRepository = userRepository;
    this.sessionRepository=sessionRepository;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserValidator userValidator = new UserValidator();

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    if (userValidator.isValid(email, password)) {
      authorize(email, password, response);
    } else {
      response.sendRedirect("/login?errorMsg=<h2 style='color:red'>Username or password invalid format!</h2>");
    }
  }

  private void authorize(String email, String password, HttpServletResponse response) throws IOException, ServletException {
    boolean authorize=userRepository.authorize(email,password);
    if (authorize) {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(email,uuid);
        sessionRepository.createSession(session);
        Cookie cookie = new Cookie("sessionId", uuid);
        cookie.setMaxAge(300);
        response.addCookie(cookie);
        response.sendRedirect("/useraccount");
    } else {
      response.sendRedirect("/login?errorMsg=<h2 style='color:red'>Wrong username or password!</h2");
    }
  }
}
