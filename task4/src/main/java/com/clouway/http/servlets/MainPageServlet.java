package com.clouway.http.servlets;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class MainPageServlet extends HttpServlet {
    private String pageForUser;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        try {
            pageForUser = Files.toString(new File("src/main/java/com/clouway/http/resources/index.html"), Charsets.UTF_8);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/html");
            response.getWriter().println(pageForUser);
        } catch (IOException e) {
            response.sendError(404);
        }
    }
}
