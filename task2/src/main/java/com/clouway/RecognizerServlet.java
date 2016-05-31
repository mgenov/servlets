package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by com.clouway on 13.05.16.
 */

public class RecognizerServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Set<String> trustedPages = new HashSet<String>();
    trustedPages.add("Abv");
    trustedPages.add("Yahoo");
    trustedPages.add("Gmail");

    String requestedPage = req.getParameter("page");

    PrintWriter out = resp.getWriter();

    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Session Test Servlet</title></head><body>");
    if (trustedPages.contains(requestedPage)) {
      out.println("<h2 style=\"color:blue;\">Request from " + requestedPage + " page</h2>");
    } else {
      out.println("<h2 style=\"color:red;\">Request from untrusted page!</h2>");
    }
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
