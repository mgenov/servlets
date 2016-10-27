package com.clouway.http.servlets;

import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.HtmlTemplate;
import com.clouway.core.Template;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.adapter.jdbc.PersistentAccountRepository;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class LoginPageServlet extends HttpServlet {
  private AccountRepository repository;
  private Template template;

  @Override
  public void init() throws ServletException {
    ConnectionProvider provider = new ConnectionProvider("jdbc:mysql://localhost/myBank", "root", "123123");
    DataStore dataStore = new DataStore(provider);
    repository = new PersistentAccountRepository(dataStore);
    try {
      String page = Files.toString(new File("src/main/resources/login.html"), Charsets.UTF_8);
      template = new HtmlTemplate(page);
      template.put("error", "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Ignore
  public LoginPageServlet() {
  }

  public LoginPageServlet(AccountRepository repository, Template template) {
    this.repository = repository;
    this.template = template;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    getInnerHtml(resp, HttpServletResponse.SC_OK, template.evaluate());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String pswd = req.getParameter("password");
    Optional<Account> optional = repository.getByName(name);

    if (optional.isPresent()) {
      Account account = optional.get();
      if(pswd.equals(account.password)) {
        resp.sendRedirect("/account");
      } else {
        template.put("error", "Wrong password");
        getInnerHtml(resp, HttpServletResponse.SC_BAD_REQUEST, template.evaluate());
      }
    } else {
      template.put("error", "Wrong username");
      getInnerHtml(resp, HttpServletResponse.SC_BAD_REQUEST, template.evaluate());
    }
  }

  private void getInnerHtml(HttpServletResponse resp, Integer statusCode, String html) throws IOException {
    resp.setContentType("text/html");
    resp.setStatus(statusCode);
    PrintWriter writer = resp.getWriter();
    writer.print(html);
    writer.flush();
  }
}
