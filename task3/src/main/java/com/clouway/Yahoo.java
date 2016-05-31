package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 17.05.16.
 */
@WebServlet(name = "Yahoo")
public class Yahoo extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String atr = "Yahoo";
    String visited = (String) session.getAttribute(atr);
    String helloMessage = "";

    if (visited == null) {
      helloMessage = "<h1>Welcome! You visited " + atr + " post service for the first time!</h1>";
    }

    session.setAttribute(atr, "visited");

    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>" + atr + " post service</title></head><body>");
    out.println(helloMessage);
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
