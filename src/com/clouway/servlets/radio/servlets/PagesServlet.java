package com.clouway.servlets.radio.servlets;

import com.clouway.servlets.radio.Core.Template;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class PagesServlet extends HttpServlet {
  private final Map<String, String> pages;
  private final Template template;
  private List<String> visited = new LinkedList<>();

  public PagesServlet(Template template, Map<String, String> pages) {
    this.template = template;
    this.pages = pages;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      String link = request.getParameter("link");
      template.setTemplateValue(getPage(link));
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      PrintWriter writer = response.getWriter();
      writer.print(template.evaluate());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getPage(String link) throws IOException {
    if (link != null && link.contains("page")) {
      setMsg(link);
      return pages.get(link);
    }
    return pages.get("index");
  }

  public void setMsg(String link) {
    if (!visited.contains(link)) {
      template.put("greeting", "Welcome to " + link + ".");
      visited.add(link);
    } else {
      template.put("greeting", "");
    }
  }
}