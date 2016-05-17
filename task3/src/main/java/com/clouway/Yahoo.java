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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String yahooVisited = (String) session.getAttribute("yahooVisited");
    String helloMessage = "";

    if (yahooVisited == null) {
      helloMessage = "Welcome! You visited Yahoo post service for the first time!";
    }

    session.setAttribute("yahooVisited", "true");

    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Yahoo post service</title></head><body>");
    out.println("<h1>" + helloMessage + "</h1>");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
