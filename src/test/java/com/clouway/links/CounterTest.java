package com.clouway.links;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class CounterTest {
    private Counter counter;
    private ByteArrayOutputStream out;
    private PrintWriter writer;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession session;

    @Before
    public void setUp() {
        counter = new Counter();
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out);
    }

    @After
    public void tearDown() throws IOException {
        writer.close();
        out.close();
    }

    @Test
    public void oneClickOnFirstLink() throws ServletException, IOException {

        context.checking(new Expectations() {{
            oneOf(request).getSession();
            will(returnValue(session));
            oneOf(session).getId();
            will(returnValue("1"));
            oneOf(request).getParameter("link");
            will(returnValue(null));
            oneOf(response).setContentType("text/html");
            oneOf(response).getWriter();
            will(returnValue(writer));

            oneOf(request).getSession();
            will(returnValue(session));
            oneOf(session).getId();
            will(returnValue("1"));
            oneOf(request).getParameter("link");
            will(returnValue("1"));
            oneOf(response).setContentType("text/html");
            oneOf(response).getWriter();
            will(returnValue(writer));
        }});
        counter.doGet(request, response);
        writer.flush();

        out.reset();

        counter.doGet(request, response);
        writer.flush();
        String expectedResult = out.toString();

        assertThat(expectedResult, containsString("<a href='?link=1'>link1</a><p>count: 1</p></br>"));
        assertThat(expectedResult, containsString("<a href='?link=2'>link2</a><p>count: 0</p></br>"));
        assertThat(expectedResult, containsString("<a href='?link=3'>link3</a><p>count: 0</p></br>"));
    }

    @Test
    public void callsFromDifferentSessions() throws ServletException, IOException {

        context.checking(new Expectations() {{
            oneOf(request).getSession();
            will(returnValue(session));
            oneOf(session).getId();
            will(returnValue("1"));
            oneOf(request).getParameter("link");
            will(returnValue(null));
            oneOf(response).setContentType("text/html");
            oneOf(response).getWriter();
            will(returnValue(writer));

            oneOf(request).getSession();
            will(returnValue(session));
            oneOf(session).getId();
            will(returnValue("1"));
            oneOf(request).getParameter("link");
            will(returnValue("1"));
            oneOf(response).setContentType("text/html");
            oneOf(response).getWriter();
            will(returnValue(writer));

            oneOf(request).getSession();
            will(returnValue(session));
            oneOf(session).getId();
            will(returnValue("2"));
            oneOf(request).getParameter("link");
            will(returnValue(null));
            oneOf(response).setContentType("text/html");
            oneOf(response).getWriter();
            will(returnValue(writer));
        }});
        counter.doGet(request, response);
        writer.flush();

        out.reset();

        counter.doGet(request, response);
        writer.flush();

        out.reset();

        counter.doGet(request, response);
        writer.flush();
        String expectedResult = out.toString();

        assertThat(expectedResult, containsString("<a href='?link=1'>link1</a><p>count: 0</p></br>"));
        assertThat(expectedResult, containsString("<a href='?link=2'>link2</a><p>count: 0</p></br>"));
        assertThat(expectedResult, containsString("<a href='?link=3'>link3</a><p>count: 0</p></br>"));
    }
}