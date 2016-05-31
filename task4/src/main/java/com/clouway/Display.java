package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 18.05.16.
 */
public class  Display extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter out = resp.getWriter();
    String name = req.getParameter("servletName");
    if (req.getAttribute("errorMsg") != null) {
      out.println("<h1 style=\"color:red\">"+req.getAttribute("errorMsg")+"</h1>");
      out.flush();
      out.close();
    } else {
      out.println("<h1 style=\"color:blue\">Request from <span style=\"color:red\">" + name + "</span> servlet!</h1>");
      out.flush();
      out.close();
    }
  }
}
