package com.clouway.link.trace;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RequestTracer extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/display");
    dispatcher.forward(request, response);
  }
}
