package com.clouway.http.servlets;

import com.clouway.bankrepository.PersistentCustomerRepository;
import com.clouway.bankrepository.PersistentSessionRepository;
import com.clouway.core.*;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class LoginPageServlet extends HttpServlet {
    private String pageForUser;
    private String pageTemplate;
    private String wrongInput = "<div class=\"alert alert-danger\"><strong>Username or password don't match!</strong></div>";
    private Template template;
    private SessionRepository sessionRepository;
    private CustomerRepository customerRepository;

    public LoginPageServlet() {

    }

    public LoginPageServlet(Template template, CustomerRepository customerRepository, SessionRepository sessionRepository) {
        this.template = template;
        this.customerRepository = customerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void init() throws ServletException {
        ConnectionProvider provider = new ConnectionProvider("bank", "postgres", "123");
        DataStore dataStore = new DataStore(provider);
        customerRepository = new PersistentCustomerRepository(dataStore);
        sessionRepository = new PersistentSessionRepository(dataStore);
        pageTemplate = getResource("login.html");
        template = new HtmlTemplate(pageTemplate);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            pageForUser = setWarning("");
            write(response, HttpServletResponse.SC_OK);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String username = request.getParameter("name");
            String password = request.getParameter("password");
            Optional<Customer> customer = customerRepository.getByName(username);
            if ((customer.isPresent()) && (customer.get().password.equals(password))) {
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(300);

                UUID uuid = UUID.randomUUID();
                String sUuid = uuid.toString();
                String sid = sha1(sUuid);

                Cookie cookie = new Cookie("sid", sid);
                response.addCookie(cookie);

                Timestamp timestamp =new Timestamp(Calendar.getInstance().getTime().getTime());
                timestamp.setTime(timestamp.getTime() + 300000);
                sessionRepository.save(new Session(cookie.getValue(), username, timestamp));

                RequestDispatcher dispatcher = request.getRequestDispatcher("/personal");
                dispatcher.forward(request, response);
            } else {
                pageForUser = setWarning(wrongInput);
                write(response,HttpServletResponse.SC_UNAUTHORIZED);
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

    private void write(HttpServletResponse response, Integer status) throws IOException {
        response.setStatus(status);
        response.setContentType("text/html");
        response.getWriter().println(pageForUser);
    }

    private String setWarning(String warning) throws IOException {
        template.put("warning", warning);
        return template.evaluate();
    }

    private String sha1(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
