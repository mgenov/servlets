package com.clouway.webapp.pages;

import org.eclipse.jetty.http.HttpHeaders;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServletDisplay extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    String header = req.getHeader(HttpHeaders.REFERER);

    writer.println("The name of the page that send request is: " + header);
    writer.flush();
  }
}
