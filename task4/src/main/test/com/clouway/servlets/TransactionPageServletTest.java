package com.clouway.servlets;

import com.clouway.core.Customer;
import com.clouway.core.CustomerRepository;
import com.clouway.core.Provider;
import com.clouway.core.Template;
import com.clouway.http.servlets.TransactionPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransactionPageServletTest {
    private JUnit4Mockery context = new JUnit4Mockery();
    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);
    private HttpSession session = context.mock(HttpSession.class);
    private Template template = context.mock(Template.class);
    private CustomerRepository repository = context.mock(CustomerRepository.class);
    private TransactionPageServlet transactionPageServlet = new TransactionPageServlet(template, repository);

    @Test
    public void happyPath() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {
            {
                oneOf(request).getSession(false);
                will(returnValue(session));
                oneOf(session).getAttribute("username");
                will(returnValue("Borislav"));

                oneOf(template).put("username", "Borislav");
                oneOf(template).put("warning", "");
                oneOf(template).evaluate();
                will(returnValue("User is Borislav"));

                oneOf(response).setContentType("text/html");
                oneOf(response).setStatus(200);
                oneOf(response).getWriter();
                will(returnValue(printWriter));
            }
        });

        transactionPageServlet.doGet(request, response);
        String page = stringWriter.toString();
        assertThat(page, containsString("User is Borislav"));
    }

    @Test
    public void deposit() throws IOException, ServletException {
        Customer customer = new Customer(1, "Borislav", "mypass", 2000);
        context.checking(new Expectations() {{
            oneOf(request).getSession(false);
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("transaction");
            will(returnValue("deposit"));
            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.of(customer)));

            oneOf(request).getParameter("amount");
            will(returnValue("500"));
            oneOf(repository).updateBalance("Borislav", 2500);

            oneOf(response).sendRedirect("/personal");
        }});

        transactionPageServlet.doPost(request, response);
    }

    @Test
    public void withdraw() throws IOException, ServletException {
        Customer customer = new Customer(1, "Borislav", "mypass", 2000);

        context.checking(new Expectations() {{
            oneOf(request).getSession(false);
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("transaction");
            will(returnValue("withdraw"));
            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.of(customer)));

            oneOf(request).getParameter("amount");
            will(returnValue("500"));
            oneOf(repository).updateBalance("Borislav", 1500);

            oneOf(response).sendRedirect("/personal");
        }});

        transactionPageServlet.doPost(request, response);
    }

    @Test
    public void insufficientFunds() throws IOException, ServletException {
        Customer customer = new Customer(1, "Borislav", "mypass", 2000);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        String insufficientFunds = "<div class=\"alert alert-danger\"><strong>Insufficient funds!</strong></div>";

        context.checking(new Expectations() {{
            oneOf(request).getSession(false);
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("transaction");
            will(returnValue("withdraw"));
            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.of(customer)));

            oneOf(request).getParameter("amount");
            will(returnValue("2500"));

            oneOf(template).put("username", "Borislav");
            oneOf(template).put("warning", insufficientFunds);
            oneOf(template).evaluate();
            will(returnValue("Insufficient funds!"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(400);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        transactionPageServlet.doPost(request, response);
        String page = stringWriter.toString();
        assertThat(page, containsString("Insufficient funds!"));
    }

    @Test
    public void customerNotPresent() throws IOException {
        context.checking(new Expectations() {{
            oneOf(request).getSession(false);
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("transaction");
            will(returnValue("withdraw"));

            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.empty()));

            oneOf(response).sendRedirect("/main");
        }});
    }
}
