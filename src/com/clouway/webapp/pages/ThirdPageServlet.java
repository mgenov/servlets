package com.clouway.webapp.pages;

import com.clouway.templates.HtmlHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ThirdPageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/third.html");

    writer.println(helper.loadResource());
    writer.flush();
  }
}
