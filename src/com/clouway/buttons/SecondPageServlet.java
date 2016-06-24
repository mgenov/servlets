package com.clouway.buttons;

import com.clouway.helper.HtmlHelper;
import com.clouway.helper.HtmlTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SecondPageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    HttpSession session = req.getSession(true);

    HtmlHelper helper = new HtmlHelper("web/WEB-INF/second.html");
    String html = helper.loadResource();

    HtmlTemplate template = new HtmlTemplate(html);
    template.put("greeting", "");

    if (session.isNew()) {
      template.put("greeting", "WELCOME!!!");
    }
    html = template.evaluate();

    writer.write(html);
    writer.flush();
  }
}

