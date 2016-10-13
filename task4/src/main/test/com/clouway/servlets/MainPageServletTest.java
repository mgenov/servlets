package com.clouway.servlets;

import com.clouway.http.servlets.MainPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class MainPageServletTest {
    public JUnit4Mockery context = new JUnit4Mockery();
    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);

    @Test
    public void happyPath() throws ServletException, IOException {
        MainPageServlet recognizerServlet = new MainPageServlet();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {
            {
                oneOf(response).setContentType("text/html");
                oneOf(response).setStatus(200);
                oneOf(response).getWriter();
                will(returnValue(printWriter));
            }
        });

        recognizerServlet.doGet(request, response);
        String page = stringWriter.toString();
        CharSequence firstExpected = "<title>CTEF Bank</title>";
        CharSequence secondExpected = "<p style=\"text-align: right;\">Users online: ${users}</p>";
        assertTrue(page.contains(firstExpected));
        assertTrue(page.contains(secondExpected));
    }
}
