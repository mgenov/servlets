package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LoginFilter;
import com.clouway.bank.adapter.http.SecurityFilter;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Time;
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

    private SessionRepository sessionRepository = context.mock(SessionRepository.class);
    private Time time = context.mock(Time.class);

    @Test
    public void sessionIsActive() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);
        final Session session = new Session("sessionId", "user@abv.bg", getTime("07"));
        final Cookie cookie = new Cookie("id", session.sessionId);
        final Cookie[] cookies = new Cookie[]{cookie};

        context.checking(new Expectations() {{
            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(session.sessionId);
            will(returnValue(session));

            oneOf(time).getCurrentTime();
            will(returnValue(getTime("00")));

            oneOf(response).sendRedirect("/login");
        }});

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void sessionIsTimeout() throws Exception {
        SecurityFilter filter = new SecurityFilter(sessionRepository, time);
        final Session session = new Session("sessionId", "user@abv.bg", getTime("00"));
        final Cookie cookie = new Cookie("id", session.sessionId);
        final Cookie[] cookies = new Cookie[]{cookie};

        context.checking(new Expectations() {{
            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(session.sessionId);
            will(returnValue(session));

            oneOf(time).getCurrentTime();
            will(returnValue(getTime("01")));

            oneOf(sessionRepository).remove(session.sessionId);

            oneOf(filterChain).doFilter(request, response);
        }});

        filter.doFilter(request, response, filterChain);

    }

    @Test
    public void noCookies() throws Exception {
        SecurityFilter loginFilter = new SecurityFilter(sessionRepository, time);
        final Cookie[] cookies = new Cookie[]{};

        context.checking(new Expectations() {{
            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(response).sendRedirect("/login");
        }});
        loginFilter.doFilter(request, response, filterChain);
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
