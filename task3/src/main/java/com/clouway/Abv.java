package com.clouway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by clouway on 17.05.16.
 */
@WebServlet(name = "Abv")
public class Abv extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String abvVisited = (String) session.getAttribute("abvVisited");
    String helloMessage = "";

    if (abvVisited == null) {
      helloMessage = "Welcome! You visited Abv post service for the first time!";
    }

    session.setAttribute("abvVisited", "true");

    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<title>Abv post service</title></head><body>");
    out.println("<h1>" + helloMessage + "</h1>");
    out.println("</body></html>");
    out.flush();
    out.close();
  }
}
