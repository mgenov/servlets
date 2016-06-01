package com.clouway;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clouway on 18.05.16.
 */
public class Dispatcher extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("/display");
    Set<String> trustedPages = new HashSet<String>();
    trustedPages.add("first");
    trustedPages.add("second");
    trustedPages.add("third");

    if (trustedPages.contains(req.getParameter("servletName"))) {
      requestDispatcher.forward(req, resp);
    } else {
      req.setAttribute("errorMsg", "Request from unknown servlet!");
      requestDispatcher.forward(req, resp);
    }
  }
}
