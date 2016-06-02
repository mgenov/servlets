package com.clouway.adapter.http;


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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String errorMessage = request.getParameter("errorMsg");
    printPage(response.getWriter(), errorMessage);
  }

  private void printPage(PrintWriter out, String errorMsg) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>LoginPage Form</title></head><body>");
    out.println("<form action=\"/logincontroller\" method=\"post\">");
    out.println("Email:<input type=\"text\" name=\"email\"/><br/>");
    out.println("Password:<input type=\"password\" name=\"password\"/><br/>");
    out.println("<input type=\"submit\" value=\"login\">");
    out.println("</form>");
    out.print("<a href=\"/register\">Register</a>");
    if (errorMsg != null) {
      out.println("<h2 style='color:red'>" + errorMsg + "</h2>");
    }
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
