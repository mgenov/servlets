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

    String errorMessage = req.getParameter("errorMsg");
    printPage(resp.getWriter(), errorMessage);
  }

  private void printPage(PrintWriter out, String errorMessage) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
    out.println("<link href='https://fonts.googleapis.com/css?family=Passion+One' rel='stylesheet' type='text/css'>");
    out.println("<link href='https://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>");
    out.println("<title>Registration Form</title></head><body>");
    out.println("<div class=\"container\">");
    out.println("<div class=\"row main\">");
    out.println("<div class=\"panel-heading\">");
    out.println("<div class=\"panel-title text-center\">");
    out.println("<h1 style='color:#3366BB' class=\"title\">Registration Form</h1>");
    out.println("<hr />");
    out.println("</div>");
    out.println("</div>");
    out.println("<div class=\"main-login main-center\">");
    out.println("<form class=\"form-horizontal\" method=\"post\" action=\"/registercontroller\">");
    out.println("<div class=\"form-group\">");
    out.println("<label for=\"username\" class=\"cols-sm-2 control-label\">Username</label>");
    out.println("<div class=\"cols-sm-10\">");
    out.println("<div class=\"input-group\">");
    out.println("<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></i></span>");
    out.println("<input type=\"text\" class=\"form-control\" name=\"username\" id=\"username\"  placeholder=\"Enter your Username\"/>");
    out.println("</div>");
    out.println("</div>");
    out.println("</div>");
    out.println("<div class=\"form-group\">");
    out.println("<label for=\"email\" class=\"cols-sm-2 control-label\">Your Email</label>");
    out.println("<div class=\"cols-sm-10\">");
    out.println("<div class=\"input-group\">");
    out.println("<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-envelope\" aria-hidden=\"true\"></i></span>");
    out.println("<input type=\"text\" class=\"form-control\" name=\"email\" id=\"email\"  placeholder=\"Enter your Email\"/>");
    out.println("</div>");
    out.println("</div>");
    out.println("</div>");
    out.println("<div class=\"form-group\">");
    out.println("<label for=\"password\" class=\"cols-sm-2 control-label\">Password</label>");
    out.println("<div class=\"cols-sm-10\">");
    out.println("<div class=\"input-group\">");
    out.println("<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-lock\" aria-hidden=\"true\"></i></span>");
    out.println("<input type=\"password\" class=\"form-control\" name=\"password\" id=\"password\"  placeholder=\"Enter your Password\"/>");
    out.println("</div>");
    out.println("</div>");
    out.println("</div>");
    out.println("<div class=\"form-group \">");
    out.println("<button type=\"submit\" class=\"btn btn-primary btn-lg btn-block login-button\">Register</button>");
    if (errorMessage != null) {
      out.println("<h3 style='color:red'>" + errorMessage + "</h3>");
    }
    out.println("</div>");
    out.println("</form>");
    out.println("</div>");
    out.println("</div>");
    out.println("</div> ");
    out.println("</body></html>");
    out.close();
  }
}
