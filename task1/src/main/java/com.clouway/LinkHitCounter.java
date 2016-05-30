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
 * Created by clouway on 13.05.16.
 */


public class LinkHitCounter extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter out = resp.getWriter();

    HttpSession session = req.getSession();

    String paramValue = req.getParameter("link");

    Map<String, Integer> visitedPages = (Map<String, Integer>) session.getAttribute("links");

    if (paramValue == null) {
      visitedPages = new HashMap<String, Integer>();
      session.setAttribute("links", visitedPages);
    } else if (!visitedPages.containsKey(paramValue)) {
      visitedPages.put(paramValue, 1);
    } else {
      Integer num = visitedPages.get(paramValue);
      visitedPages.put(paramValue, num + 1);
    }

    printPage(out, visitedPages);
  }


  public void printPage(PrintWriter out, Map<String, Integer> visitedPages) {
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Session Test Servlet</title></head><body>");
    out.println("<p><a  href=\"linkhitcounter?link=first\">ABV</a>");
    if (visitedPages.containsKey("first")) {
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + visitedPages.get("first") + " times.</h2>");
    }
    out.println("<p><a  href=\"linkhitcounter?link=second\">GMAIL</a>");
    if (visitedPages.containsKey("second")) {
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + visitedPages.get("second") + " times.</h2>");
    }
    out.println("<p><a  href=\"linkhitcounter?link=third\">YAHOO</a>");
    if (visitedPages.containsKey("third")) {
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + visitedPages.get("third") + " times.</h2>");
    }
    out.println("</body></html>");
    out.close();
  }
}
