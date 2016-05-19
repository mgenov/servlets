package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 19.05.16.
 */
@WebServlet(name = "Register")
public class Register extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doGet(request,response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Registration Form</title></head><body>");
    out.println("<form action=\"/welcomehomepage\" method=\"post\">");
    out.println("Name:<input type=\"text\" name=\"regname\"/><br/>");
    out.println("Password:<input type=\"password\" name=\"regpassword\"/><br/>");
    out.print("<input type=\"submit\" value=\"submit\">");
    out.println("</body></html>");
  }
}
