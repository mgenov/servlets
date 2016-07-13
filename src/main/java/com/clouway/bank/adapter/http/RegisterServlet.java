package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.Validator;
import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class RegisterServlet extends HttpServlet {
    private final UserRepository repository;
    private final Validator<User> validator;
    private String html;
    private HtmlTemplate template;
    private final AccountRepository accountRepository;

    public RegisterServlet(UserRepository repository, Validator<User> validator, AccountRepository accountRepository) {
        this.repository = repository;
        this.validator = validator;
        this.accountRepository = accountRepository;
    }

    @Override
    public void init() throws ServletException {
        HtmlHelper helper = new HtmlHelper("web/WEB-INF/register.html");
        html = helper.loadResource();
        template = new HtmlTemplate(html);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String repeatedPassword = req.getParameter("confirm");

        User user = new User(name, email, password);
        String messages = validator.validate(user);
        if (!(messages.equals("") && password.equals(repeatedPassword))) {
            resp.sendRedirect("/register?errorMessage=" + messages);
        }
        if (repository.findByEmail(user.email) != null) {
            resp.sendRedirect("/register?errorMessage=User with this email already exist");
        } else {
            repository.register(user);

            accountRepository.createAccount(new Account(email, 0.00));

            resp.sendRedirect("/login");
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        template = new HtmlTemplate(html);
        template.put("message", "");

        String errors = req.getParameter("errorMessage");
        if (errors != null) {
            template.put("message", "<div class=\"alert alert-danger\">" + errors + "</div>");
        }

        writer.println(template.evaluate());
        writer.flush();
    }
}

