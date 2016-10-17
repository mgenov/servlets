package com.clouway.http.servlets;

import com.clouway.bankrepository.PersistentCustomerRepository;
import com.clouway.core.Customer;
import com.clouway.core.CustomerRepository;
import com.clouway.core.HtmlTemplate;
import com.clouway.core.Template;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class RegisterPageServlet extends HttpServlet {
    private String pageForUser;
    private String pageTemplate;
    private String usernameTaken = "<div class=\"alert alert-danger\"><strong>Username is taken, please try another!</strong></div>";
    private Template template;
    private CustomerRepository customerRepository;

    public RegisterPageServlet() {

    }

    public RegisterPageServlet(CustomerRepository customerRepository, Template template) {
        this.customerRepository = customerRepository;
        this.template = template;
    }

    @Override
    public void init() throws ServletException {
        ConnectionProvider provider = new ConnectionProvider("bank", "postgres", "123");
        DataStore dataStore = new DataStore(provider);
        customerRepository = new PersistentCustomerRepository(dataStore);
        pageTemplate = getResource("register.html");
        template = new HtmlTemplate(pageTemplate);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            template.put("warning", "");
            pageForUser = template.evaluate();

            write(response);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        Optional<Customer> customer = customerRepository.getByName(username);
        try {
            if (!customer.isPresent()) {
                customerRepository.register(new Customer(null, username, password, 0));

                response.sendRedirect("/");
            } else {
                template.put("warning", usernameTaken);
                pageForUser = template.evaluate();

                write(response);
            }
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String getResource(String page) {
        String result = "";
        try {
            result = Files.toString(new File("src/main/resources/" + page), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void write(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        response.getWriter().println(pageForUser);
    }
}
