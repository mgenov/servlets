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
@WebServlet(name = "RegisterPage")
public class RegisterPage extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String errorMessage = "";

    if (req.getParameter("errorMsg") != null) {
      errorMessage = req.getParameter("errorMsg");
    }
    printPage(resp.getWriter(), errorMessage);
  }

  private void printPage(PrintWriter out, String errorMessage) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Registration Form</title></head><body>");
    out.println("<form action=\"/registercontroller\" method=\"post\">");
    out.println("Username:<input type=\"text\" name=\"regname\"/><br/>");
    out.println("Password:<input type=\"password\" name=\"regpassword\"/><br/>");
    out.println("Email:<input type=\"text\" name=\"email\"/><br/>");
    out.print("<input type=\"submit\" value=\"submit\">");
    out.println(errorMessage);
    out.println("</body></html>");
    out.close();
  }
}
