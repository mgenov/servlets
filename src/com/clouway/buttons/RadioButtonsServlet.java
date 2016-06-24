package com.clouway.buttons;

import com.clouway.helper.HtmlHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class RadioButtonsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    String page = req.getParameter("page");

    HtmlHelper helper = new HtmlHelper("web/WEB-INF/radioButtons.html");
    if (page != null) {
      resp.sendRedirect(page);
    }
    writer.println(helper.loadResource());
  }
}
