package com.clouway.bank.http;

import com.clouway.utility.Template;

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
@WebServlet(name = "LoginPage")
public class LoginPage extends HttpServlet {

  private Template template;

  public LoginPage(Template template) {
    this.template = template;
  }

  @Override
  public void init() throws ServletException {
    template.loadFromFile("web/WEB-INF/pages/Login.html");

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    template.put("message", "");
    String message = (String) request.getParameter("message");
    if (message != null) {
      template.put("message", message);
    }
    PrintWriter writer = response.getWriter();
    response.setContentType("text/html");
    writer.write(template.evaluate());
  }
}
