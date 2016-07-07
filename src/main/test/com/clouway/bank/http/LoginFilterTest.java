package com.clouway.bank.http;

import com.clouway.bank.adapter.http.LoginFilter;
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
public class LoginFilterTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);
    private FilterChain filterChain = context.mock(FilterChain.class);

    private SessionRepository sessionRepository = context.mock(SessionRepository.class);
    private Time time = context.mock(Time.class);

    @Test
    public void happyPath() throws Exception {
        LoginFilter filter = new LoginFilter(sessionRepository, time);
        final Session session = new Session("sessionId", "user@abv.bg", getTime("12:12:0000"));
        final Cookie cookie = new Cookie("id", "sessionId");
        final Cookie[] cookies = new Cookie[]{cookie};

        context.checking(new Expectations() {{
            oneOf(sessionRepository).remove();

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(sessionRepository).findSessionById(session.sessionId);
            will(returnValue(session));

            oneOf(time).getCurrentTime();
            will(returnValue(getTime("13:13:1313")));

            oneOf(filterChain).doFilter(request, response);

            oneOf(response).sendRedirect("/login");
        }});

        filter.doFilter(request, response, filterChain);
    }

    private Long getTime(String dateAsString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ssss");
        Date date = null;
        try {
            date = dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
