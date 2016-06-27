package com.clouway.links;

import com.clouway.templates.HtmlTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServletAccessCounter extends HttpServlet {
  private Map<String, Integer> links = new ConcurrentHashMap<String, Integer>();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String link = req.getParameter("link");
    countLinkAccess(link);

    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");

    getHtml("web/WEB-INF/links.html", writer);
    writer.flush();
  }

  public String getHtml(String path, PrintWriter writer) {
    StringBuffer buffer = new StringBuffer();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }

      String html = buffer.toString();
      HtmlTemplate template = new HtmlTemplate(html);
      template.put("link1", links.get("https://github.com/clouway") + " times");
      template.put("link2", links.get("http://tutorials.jenkov.com") + " times");
      template.put("link3", links.get("http://www.tutorialspoint.com") + " times");

      String evaluationValue = template.evaluate();
      buffer.append(evaluationValue);

      writer.write(evaluationValue);
      writer.flush();

      return buffer.toString();
    } catch (IOException e) {
      return "";
    }
  }

  private void countLinkAccess(String link) {
    if (link != null) {
      if (!links.containsKey(link)) {
        links.put(link, 1);
      } else {
        links.put(link, links.get(link) + 1);
      }
    }
  }
}

