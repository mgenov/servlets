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
    out.println("<title>LoginPage Form</title></head><body>");
    out.println("<h1> Welcome! </h1>");
    out.println("<form action=\"/bankoperationhandler\" method=\"post\">");
    out.println("Amount:<input type=\"text\" name=\"amount\"/><br/>");
    out.print("<input type=\"submit\" name=\"operation\" value=\"Deposit\">");
    out.print("<input type=\"submit\" name=\"operation\" value=\"Withdraw\">");
    out.println("</form>");
    out.print("<form action=\"/logoutcontroller\" method=\"post\">");
    out.println("<input type=\"submit\" value=\"Logout\">");
    out.println("<a href=\"/history?page=1\">History of Transactions</a>");
    out.println("<h3>Total amount: " + fundsRepository.getBalance(email) + "</h3>");
    if (message != null) {
      out.println("<h3>" + message + "</h3>");
    }
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
