package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 31.05.16.
 */
public class LinkHitCounter extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter out = resp.getWriter();
    HttpSession session = req.getSession();
    Map<String, Integer> visitedPages = (Map<String, Integer>) session.getAttribute("links");
    String paramValue = req.getParameter("link");

    if (paramValue == null) {
      printPage(out, new HashMap<String, Integer>());
    }

    if (visitedPages == null) {
      visitedPages = new HashMap<String, Integer>();
      session.setAttribute("links", visitedPages);
      printPage(out, visitedPages);
      return;
    }

    if (visitedPages.containsKey(paramValue)) {
      Integer counter = visitedPages.get(paramValue);
      visitedPages.put(paramValue, counter + 1);
      session.setAttribute("links", visitedPages);
      printPage(out, visitedPages);
    } else {
      visitedPages.put(paramValue, 1);
      session.setAttribute("links", visitedPages);
      printPage(out, visitedPages);
    }
  }


  private void printPage(PrintWriter out, Map<String, Integer> visitedPages) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Session Test Servlet</title></head><body>");
    out.println("<p><a  href=\"linkhitcounter?link=first\">ABV</a>");
    out.println("<p><a  href=\"linkhitcounter?link=second\">Yahoo</a>");
    out.println("<p><a  href=\"linkhitcounter?link=third\">Gmail</a>");

    for (String pageKey : visitedPages.keySet()) {
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + visitedPages.get(pageKey) + " times.</h2>");
    }

    out.println("</body></html>");
    out.close();
  }
}
