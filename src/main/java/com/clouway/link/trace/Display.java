package com.clouway.link.trace;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Display extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String referer = request.getHeader("referer");

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<body>");

    if(referer==null){
      out.println("<h1>directly accessed</h1>");
    }

    else {
      out.println("<h1>" + referer + "</h1>");
      out.println("<h2>visited the link</h2>");
    }
    
    out.println("</body>");
    out.println("</html>");
  }
}
