package com.clouway.bank.http;

import com.clouway.bank.adapter.http.SecurityFilter;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Time;
import com.google.common.base.Optional;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SecurityFilterTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);
    private FilterChain filterChain = context.mock(FilterChain.class);
    private Time time = context.mock(Time.class);

    private SessionRepository sessionRepository = context.mock(SessionRepository.class);

    @Test
    public void login() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);

        final Cookie cookie = new Cookie("id", "sessionId");
        final Cookie[] cookies = new Cookie[]{cookie};

        context.checking(new Expectations() {{
            oneOf(request).getRequestURI();
            will(returnValue("/login"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(cookie.getValue());
            will(returnValue(Optional.absent()));

            oneOf(filterChain).doFilter(request, response);
        }});

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void alreadyLogin() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);

        final Cookie cookie = new Cookie("id", "sessionId");
        final Cookie[] cookies = new Cookie[]{cookie};
        final Session session = new Session(cookie.getValue(), "user@domain.com", getTime("06"));

        context.checking(new Expectations() {{
            oneOf(request).getRequestURI();
            will(returnValue("/login"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(cookie.getValue());
            will(returnValue(Optional.of(session)));

            oneOf(time).getCurrentTime();
            will(returnValue(getTime("00")));

            oneOf(response).sendRedirect("/home");

            oneOf(filterChain).doFilter(request, response);
        }});

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void getSecurityResourceNoSession() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);
        final Cookie[] cookies = new Cookie[]{};

        context.checking(new Expectations() {{
            oneOf(request).getRequestURI();
            will(returnValue("/account"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById("");
            will(returnValue(Optional.absent()));

            oneOf(response).sendRedirect("/login");
        }});

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void removeTimeOutSessions() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);

        final Cookie cookie = new Cookie("id", "sessionId");
        final Cookie[] cookies = new Cookie[]{cookie};
        final Session session = new Session(cookie.getValue(), "user@domain.com", getTime("00"));

        context.checking(new Expectations() {{
            oneOf(request).getRequestURI();
            will(returnValue("/home"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(cookie.getValue());
            will(returnValue(Optional.of(session)));

            oneOf(time).getCurrentTime();
            will(returnValue(getTime("04")));

            oneOf(sessionRepository).remove(session.sessionId);

            oneOf(response).sendRedirect("/login");

            oneOf(filterChain).doFilter(request, response);
        }});

        filter.doFilter(request, response, filterChain);
    }

    private Long getTime(String dateAsString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
        Date date = null;
        try {
            date = dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
