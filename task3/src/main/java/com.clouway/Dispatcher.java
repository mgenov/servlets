package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by clouway on 16.05.16.
 */
@WebServlet(name = "Dispatcher")
public class Dispatcher extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String page = req.getParameter("page");
    resp.setContentType("text/html;charset=UTF-8");
    resp.sendRedirect(page);
  }
}
