package com.clouway.bank.http;

import com.clouway.bank.persistence.PersistentUserRepository;
import com.clouway.bank.core.User;
import com.clouway.bank.utils.HtmlHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
@WebServlet("register")
public class RegisterServlet extends HttpServlet {
  private final PersistentUserRepository repository;

  public RegisterServlet(PersistentUserRepository repository) {
    this.repository = repository;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/register.html");

    PrintWriter writer = resp.getWriter();

    writer.println(helper.loadResource());
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String password = req.getParameter("password");

    repository.register(new User(name, password));
  }
}
