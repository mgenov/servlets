package com.clouway.adapter.http;

import com.clouway.core.OnlineUsers;

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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    printPage(out);
  }

  public void printPage(PrintWriter out) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>LoginPage Form</title></head><body>");
    out.println("<h1> Welcome! </h1>");
    out.print("<form action=\"/logoutcontroller\" method=\"post\">");
    out.print("<input type=\"submit\" value=\"Logout\">");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
