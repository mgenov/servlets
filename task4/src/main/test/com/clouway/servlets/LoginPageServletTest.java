package com.clouway.servlets;

import com.clouway.core.*;
import com.clouway.http.servlets.LoginPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class LoginPageServletTest {
    public JUnit4Mockery context = new JUnit4Mockery();
    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);
    private CustomerRepository customerRepository = context.mock(CustomerRepository.class);
    private SessionRepository sessionRepository = context.mock(SessionRepository.class);
    private Template template = context.mock(Template.class);
    private LoginPageServlet loginPageServlet = new LoginPageServlet(template, customerRepository, sessionRepository);
    private HttpSession session = context.mock(HttpSession.class);
    private String warning = "<div class=\"alert alert-danger\"><strong>Username or password don't match!</strong></div>";

    @Test
    public void happyPath() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {{
            oneOf(template).put("warning", "");
            oneOf(template).evaluate();
            will(returnValue("Login page"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(200);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        loginPageServlet.doGet(request, response);
        String page = stringWriter.toString();
        assertThat(page, containsString("Login page"));
    }

    @Test
    public void login() throws ServletException, IOException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("name");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("password");
            will(returnValue("mypass"));

            oneOf(customerRepository).getByName("Borislav");
            will(returnValue(Optional.of(new Customer(0, "Borislav", "mypass", 0))));

            oneOf(request).getSession(true);
            will(returnValue(session));

            oneOf(session).setAttribute("username", "Borislav");
            oneOf(session).setMaxInactiveInterval(300);

            oneOf(response).addCookie(with(any(Cookie.class)));
            oneOf(sessionRepository).save(with(any(Session.class)));

            oneOf(request).getRequestDispatcher("/personal");
        }});

        loginPageServlet.doPost(request, response);
    }

    @Test
    public void getUnknown() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {{
            oneOf(request).getParameter("name");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("password");
            will(returnValue("mypass"));

            oneOf(customerRepository).getByName("Borislav");
            will(returnValue(Optional.empty()));

            oneOf(template).put("warning", warning);
            oneOf(template).evaluate();
            will(returnValue("Wrong username"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(401);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        loginPageServlet.doPost(request, response);

        String page = stringWriter.toString();

        assertThat(page, containsString("Wrong username"));
    }

    @Test
    public void wrongPassword() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {{
            oneOf(request).getParameter("name");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("password");
            will(returnValue("mypass"));

            oneOf(customerRepository).getByName("Borislav");
            will(returnValue(Optional.of(new Customer(0, "Borislav", "password", 0))));

            oneOf(template).put("warning", warning);
            oneOf(template).evaluate();
            will(returnValue("Wrong pass"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(401);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        loginPageServlet.doPost(request, response);

        String page = stringWriter.toString();

        assertThat(page, containsString("Wrong pass"));
    }
}
