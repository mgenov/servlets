package com.clouway.adapter.http;


import com.clouway.core.SessionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 19.05.16.
 */
@WebServlet(name = "LoginPage")
public class LoginPage extends HttpServlet {
  private SessionRepository sessionRepository;

  public LoginPage(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String errorMessage = request.getParameter("errorMsg");
    Integer usersOnline = sessionRepository.getActiveSessions();
    printPage(response.getWriter(), errorMessage, usersOnline);
  }

  private void printPage(PrintWriter out, String errorMsg, Integer usersOnline) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
    out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
    out.println("<title>LoginPage Form</title></head><body>");
    out.println("<div class=\"container\">");
    out.println("<form class=\"form-signin\" action=\"/logincontroller\" method=\"post\">");
    out.println("<h1 style='color:#3366BB'  class=\"form-signin-heading text-center\">Please login</h1>");
    out.println("<div class=\"container\">");
    out.println("<label for=\"inputEmail\" class=\"sr-only\">Email address</label>");
    out.println("<input type=\"email\" name=\"email\" id=\"inputEmail\" class=\"form-control\" placeholder=\"Email address\" required autofocus>");
    out.println("<label for=\"inputPassword\" class=\"sr-only\">Password</label>");
    out.println("<input type=\"password\" name=\"password\" id=\"inputPassword\" class=\"form-control\" placeholder=\"Password\" required>");
    out.print("<a href=\"/register\">Register</a>");
    out.println("<button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>");
    out.println("</form>");
    out.println("<h3 style='color:#3366BB'>Total users online: " + usersOnline + "</h3>");
    if (errorMsg != null) {
      out.println("<h3 style='color:red'>" + errorMsg + "</h3>");
    }
    out.println("</div");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
