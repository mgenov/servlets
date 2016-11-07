package com.clouway.http.servlets;

import com.clouway.bankrepository.PersistentCustomerRepository;
import com.clouway.core.*;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class TransactionPageServlet extends HttpServlet {
    private String pageForUser;
    private String pageTemplate;
    private String insufficientFunds = "<div class=\"alert alert-danger\"><strong>Insufficient funds!</strong></div>";
    private Template template;
    private CustomerRepository customerRepository;

    public TransactionPageServlet() {

    }

    public TransactionPageServlet(Template template, CustomerRepository customerRepository) {
        this.template = template;
        this.customerRepository = customerRepository;
    }

    @Override
    public void init() throws ServletException {
        ConnectionProvider provider = new ConnectionProvider("bank", "postgres", "123");
        DataStore dataStore = new DataStore(provider);
        customerRepository = new PersistentCustomerRepository(dataStore);
        pageTemplate = getResource("transaction.html");
        template = new HtmlTemplate(pageTemplate);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        template.put("username", session.getAttribute("username").toString());
        template.put("warning", "");
        pageForUser = template.evaluate();

        write(response, HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        String username = session.getAttribute("username").toString();
        String type = request.getParameter("transaction");
        Optional<Customer> optCustomer = customerRepository.getByName(username);
        if(!optCustomer.isPresent()){
            response.sendRedirect("/main");
        }
        if (type.equals("deposit")) {
            Customer customer = optCustomer.get();
            Integer amount = Integer.parseInt(request.getParameter("amount"));
            customerRepository.updateBalance(customer.name, customer.balance + amount);
            response.sendRedirect("/personal");
        }
        if(type.equals("withdraw")) {
            Customer customer = optCustomer.get();
            Integer amount = Integer.parseInt(request.getParameter("amount"));
            if (amount > customer.balance) {
                pageForUser = setWarning(username);
                write(response,HttpServletResponse.SC_BAD_REQUEST);
            } else {
                customerRepository.updateBalance(customer.name, customer.balance - amount);
                response.sendRedirect("/personal");
            }
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

    private void write(HttpServletResponse response, Integer status) {
        response.setStatus(status);
        response.setContentType("text/html");
        try {
            response.getWriter().println(pageForUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String setWarning(String username) {
        template.put("username", username);
        template.put("warning", insufficientFunds);
        return template.evaluate();
    }
}
