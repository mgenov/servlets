package com.clouway.http.servlets;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class HomePageServlet extends HttpServlet {

  @Ignore
  @SuppressWarnings("unused")
  public HomePageServlet() {
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    writer.print("Welcome");
    writer.flush();
  }
}
