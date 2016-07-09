package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Time;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SecurityFilter implements Filter {
    private final SessionRepository sessionRepository;
    private final Time time;

    public SecurityFilter(SessionRepository sessionRepository, Time time) {
        this.sessionRepository = sessionRepository;
        this.time = time;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie cookie = find(request.getCookies());

        Session session;

        if (cookie == null) {
            response.sendRedirect("/login");
            return;
        }

        session = sessionRepository.findSessionById(cookie.getValue());

        if (session != null) {
            if (session.timeForLife < time.getCurrentTime()) {
                sessionRepository.remove(session.sessionId);
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/login");
            }
        }
    }

    @Override
    public void destroy() {

    }

    private Cookie find(Cookie[] cookies) {
        for (Cookie each : cookies) {
            if (each.getName().equals("id")) {
                return each;
            }
        }
        return null;
    }
}
