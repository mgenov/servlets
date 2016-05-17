package com.clouway.radio.buttons;

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
public class RadioMenu extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String page = request.getParameter("page");

    if ("first".equals(page)) {
      response.sendRedirect("first");
    }

    else if ("second".equals(page)) {
      response.sendRedirect("second");
    }

    else if ("third".equals(page)) {
      response.sendRedirect("third");
    }

    else {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      out.println("<html>");
      out.println("<body>");
      out.println("<form method='get'>");
      out.println("<label>First</label><input type='radio' name='page' value='first'><br>");
      out.println("<label>Second</label><input type='radio' name='page' value='second'><br>");
      out.println("<label>Third</label><input type='radio' name='page' value='third'><br>");
      out.println("<input type='submit' value='submit'>");
      out.println("</form>");
      out.println("</body>");
      out.println("</html>");

    }
  }
}
