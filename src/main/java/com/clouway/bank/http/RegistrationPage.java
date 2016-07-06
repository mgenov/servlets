package com.clouway.bank.http;

import com.clouway.utility.Template;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationPage extends HttpServlet {
  private Template template;

  public RegistrationPage(Template template) {
    this.template = template;
  }

  @Override
  public void init() throws ServletException {
    template.loadFromFile("web/WEB-INF/pages/Register.html");
    template.put("registerMessage", "");
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String state = request.getParameter("state");
    String message = request.getParameter("registerMessage");
    if (state != null && message != null) {
      template.put("state", state);
      template.put("registerMessage", message);
    }
    PrintWriter writer = response.getWriter();
    response.setContentType("text/html");
    writer.write(template.evaluate());
  }

}
