package com.clouway;

import javax.servlet.ServletContext;
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
@WebServlet(name = "Login")
public class Login extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 doGet(request,response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String errorMessage= (String) request.getAttribute("errorMessage");

    if(errorMessage==null){
      errorMessage="";
    }

    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Login Form</title></head><body>");
    out.println("<form action=\"/welcomehomepage\" method=\"post\">");
    out.println("Name:<input type=\"text\" name=\"name\"/><br/>");
    out.println("Password:<input type=\"password\" name=\"password\"/><br/>");
    out.println("<input type=\"submit\" value=\"login\">");
    out.println("</form>");
    out.print("<form action=\"/register\" method=\"post\">");
    out.print("<input type=\"submit\" value=\"register\">");
    out.println("</form>");
    out.println(errorMessage);
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
