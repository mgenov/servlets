package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 18.05.16.
 */
public class FirstServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter out = resp.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>First page</title></head><body>");
    out.println("<form action=\"/dispatcher\" method=\"post\">");
    out.println("<input type=\"submit\" value=\"Request\">");
    out.println("<input type=\"hidden\" name=\"servletName\" value=\"first\"");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
