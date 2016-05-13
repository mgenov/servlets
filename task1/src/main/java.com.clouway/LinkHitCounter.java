import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by clouway on 13.05.16.
 */


public class LinkHitCounter extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html;charset=UTF-8");
    HttpSession session = req.getSession();

    String paramValue  = req.getParameter("link");

    Integer accessCount = (Integer) session.getAttribute(paramValue);


    if (accessCount == null) {
      accessCount = 1;
    } else {
      accessCount += 1;
    }

    if (paramValue != null) {
      session.setAttribute(paramValue, accessCount);
    }

    PrintWriter out = resp.getWriter();
    try {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
      out.println("<title>Session Test Servlet</title></head><body>");
      out.println("<p><a  href=\"linkhitcounter?link=first\">ABV</a>");
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + session.getAttribute("first") + " times.</h2>");
      out.println("<p><a  href=\"linkhitcounter?link=second\">GMAIL</a>");
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + session.getAttribute("second") + " times.</h2>");
      out.println("<p><a  href=\"linkhitcounter?link=third\">YAHOO</a>");
      out.println("<h2 style=\"color:blue;\">You have accessed this link " + session.getAttribute("third") + " times.</h2>");
      out.println("</body></html>");

    } finally {
      out.close();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
   doGet(req,resp);
  }
}
