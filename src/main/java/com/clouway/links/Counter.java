package com.clouway.links;

import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Counter extends HttpServlet {
    private Map<String, Integer> link1 = new HashMap<String, Integer>();
    private Map<String, Integer> link2 = new HashMap<String, Integer>();
    private Map<String, Integer> link3 = new HashMap<String, Integer>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        String linkNumber = request.getParameter("link");
        if (!Strings.isNullOrEmpty(linkNumber)) {
            switch (Integer.parseInt(linkNumber)) {
                case 1:
                    setTimesVisited(link1, sessionId);
                    break;
                case 2:
                    setTimesVisited(link2, sessionId);
                    break;
                case 3:
                    setTimesVisited(link3, sessionId);
                    break;
            }
        }

        int firstCount = link1.get(sessionId) == null ? 0 : link1.get(sessionId);
        int secondCount = link2.get(sessionId) == null ? 0 : link2.get(sessionId);
        int thirdCount = link3.get(sessionId) == null ? 0 : link3.get(sessionId);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<html>");
        out.write("<body>");
        out.write("<a href='?link=1'>link1</a><p>count: " + firstCount + "</p></br>");
        out.write("<a href='?link=2'>link2</a><p>count: " + secondCount + "</p></br>");
        out.write("<a href='?link=3'>link3</a><p>count: " + thirdCount + "</p></br>");
        out.write("</body>");
        out.write("</html>");

    }

    private void setTimesVisited(Map<String, Integer> linkCounter, String sessionId) {
        if (linkCounter.containsKey(sessionId)) {
            int visitedLink = linkCounter.get(sessionId);
            visitedLink++;
            linkCounter.put(sessionId, visitedLink);
        } else {
            linkCounter.put(sessionId, 1);
        }
    }
}
