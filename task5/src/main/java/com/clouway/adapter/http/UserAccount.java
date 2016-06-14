package com.clouway.adapter.http;

import com.clouway.core.CookieFinder;
import com.clouway.core.FundsRepository;
import com.clouway.core.OnlineUsers;
import com.clouway.core.SessionRepository;
import com.clouway.core.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 19.05.16.
 */
@WebServlet(name = "UserAccount")
public class UserAccount extends HttpServlet {
  private FundsRepository fundsRepository;
  private SessionRepository sessionRepository;
  private CookieFinder cookieFinder;

  public UserAccount(FundsRepository fundsRepository, SessionRepository sessionRepository, CookieFinder cookieFinder) {
    this.fundsRepository = fundsRepository;
    this.sessionRepository = sessionRepository;
    this.cookieFinder = cookieFinder;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    Cookie cookie = cookieFinder.find(cookies);
    String sessionId = cookie.getValue();
    String email = sessionRepository.getCurrentUserEmail(sessionId);
    String message = request.getParameter("message");
    PrintWriter out = response.getWriter();
    printPage(out, email, message);
  }

  private void printPage(PrintWriter out, String email, String message) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
    out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
    out.println("<title>User Acconunt</title></head><body>");
    out.println("<form class=\"form-signin\" action=\"/bankoperationhandler\" method=\"post\">");
    out.println("<h1 style='color:#3366BB'  class=\"form-signin-heading text-center\">You have been logged as:  " + email + "</h1>");
    out.println("<div class=\"container\">");
    out.println("<label for=\"inputAmount\" class=\"sr-only\">Amount</label>");
    out.println("<input type=\"text\" name=\"amount\" id=\"inputEmail\" class=\"form-control\" placeholder=\"Enter Amount (ex. 123.00)\" required autofocus>");
    out.print("<a href=\"/history\">Transaction History</a>");
    out.println("<input class=\"btn btn-lg btn-primary btn-block\" type=\"submit\" name=\"operation\" value=\"Deposit\" >");
    out.println("<input class=\"btn btn-lg btn-primary btn-block\" type=\"submit\" name=\"operation\" value=\"Withdraw\" >");
    out.println("</form>");
    out.println("<h3 style='color:#3366BB'>Total amount: " + fundsRepository.getBalance(email) + "</h3>");
    out.println("<form class=\"form-signin\" action=\"/logoutcontroller\" method=\"post\">");
    out.println("<input class=\"btn btn-lg btn-primary btn-block\" type=\"submit\"  value=\"Logout\" >");
    out.println("</form>");
    if (message != null) {
      out.println("<h3 style='color:#3366BB'>" + message + "</h3>");
    }
    out.println("</div");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
