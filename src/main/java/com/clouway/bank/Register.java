package com.clouway.bank;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@WebServlet(name = "Register")
public class Register extends HttpServlet {

  RegistrationController registrationController = new RegistrationController();

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String userName = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String registrationMessage = registrationController.register(userName, password, confirmPassword);

    if (registrationMessage==null){
      doGet(request, response);
    }else{
      request.setAttribute("message", registrationMessage);
      doGet(request, response);
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
    String message = (String) request.getAttribute("message");

    ServletContext context = getServletContext();
    byte[] content = ByteStreams.toByteArray(context.getResourceAsStream("/WEB-INF/pages/Register.html"));
    String pageContent = new String(content, "UTF-8");
    if (message!=null) {
     int index =  pageContent.indexOf("</form>")+7;
     String beforeMessage =  pageContent.substring(0, index);
      String afterMessage = pageContent.substring(index);
      pageContent=beforeMessage+"\n<p>"+message+"</p>\n"+afterMessage;
    }
    out.println(pageContent);
    out.flush();
  }
}
