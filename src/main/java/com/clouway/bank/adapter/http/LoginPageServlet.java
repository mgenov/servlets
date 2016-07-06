package com.clouway.bank.adapter.http;

import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class LoginPageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/login.html");
    String page = helper.loadResource();

    HtmlTemplate template = new HtmlTemplate(page);
    template.put("message", "");

    String errors = req.getParameter("errorMessage");

    if (errors != null) {
      template.put("message", errors);
    }
    PrintWriter writer = resp.getWriter();
    writer.println(template.evaluate());

    writer.flush();
  }
}
