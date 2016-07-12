package com.clouway.bank.adapter.http;

import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HomePageServlet extends HttpServlet {
    private SessionRepository sessionRepository;

    public HomePageServlet(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String pages = req.getParameter("page");

        if (!Strings.isNullOrEmpty(pages)) {
            resp.sendRedirect(pages);
        }

        HtmlHelper helper = new HtmlHelper("web/WEB-INF/home.html");
        String page = helper.loadResource();

        HtmlTemplate template = new HtmlTemplate(page);
        template.put("number", getOnlineUsers().toString());

        writer.println(template.evaluate());

        writer.flush();
    }

    private Integer getOnlineUsers() {
        return sessionRepository.countOnlineUsers();
    }
}
