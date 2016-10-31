package com.clouway.servlets;

import com.clouway.core.Customer;
import com.clouway.core.CustomerRepository;
import com.clouway.core.Template;
import com.clouway.http.servlets.RegisterPageServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class RegisterPageServletTest {

    private JUnit4Mockery context = new JUnit4Mockery();
    private HttpServletRequest request = context.mock(HttpServletRequest.class);
    private HttpServletResponse response = context.mock(HttpServletResponse.class);
    private CustomerRepository repository = context.mock(CustomerRepository.class);
    private Template template = context.mock(Template.class);
    private RegisterPageServlet registerPageServlet = new RegisterPageServlet(repository, template);

    @Test
    public void happyPath() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        context.checking(new Expectations() {{
            oneOf(template).put("warning", "");
            oneOf(template).evaluate();
            will(returnValue("Register"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(200);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        registerPageServlet.doGet(request, response);
        String page = stringWriter.toString();
        assertThat(page, containsString("Register"));
    }

    @Test
    public void registerUser() throws ServletException, IOException, SQLException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("name");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("password");
            will(returnValue("mypassword"));
            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.empty()));
            oneOf(repository).register(new Customer(null, "Borislav", "mypassword", 0));

            oneOf(response).sendRedirect("/");
        }});

        registerPageServlet.doPost(request, response);
    }

    @Test
    public void usernameExist() throws ServletException, IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        String warning = "<div class=\"alert alert-danger\"><strong>Username is taken, please try another!</strong></div>";
        context.checking(new Expectations() {{
            oneOf(request).getParameter("name");
            will(returnValue("Borislav"));
            oneOf(request).getParameter("password");
            will(returnValue("mypassword"));
            oneOf(repository).getByName("Borislav");
            will(returnValue(Optional.of(new Customer(1, "Borislav", "mypassword", 0))));

            oneOf(template).put("warning", warning);
            oneOf(template).evaluate();
            will(returnValue("Name is taken"));

            oneOf(response).setContentType("text/html");
            oneOf(response).setStatus(200);
            oneOf(response).getWriter();
            will(returnValue(printWriter));
        }});

        registerPageServlet.doPost(request, response);

        String page = stringWriter.toString();

        assertThat(page, containsString("Name is taken"));
    }
}
