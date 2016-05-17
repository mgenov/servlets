package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 17.05.16.
 */
@WebServlet(name = "Gmail")
public class Gmail extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16'>");
    out.println("<title>Gmail post service</title></head><body>");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
