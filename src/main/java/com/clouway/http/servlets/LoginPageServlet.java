package com.clouway.http.servlets;

import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.HtmlTemplate;
import com.clouway.core.ServletResponseWriter;
import com.clouway.core.Template;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.adapter.jdbc.PersistentAccountRepository;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.io.ByteStreams;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class LoginPageServlet extends HttpServlet {
  private AccountRepository repository;
  private Template template;
  private ServletResponseWriter servletResponseWriter;

  @Override
  public void init() throws ServletException {
    ConnectionProvider provider = new ConnectionProvider();
    DataStore dataStore = new DataStore(provider);
    repository = new PersistentAccountRepository(dataStore);

    try {
      String page = new String(ByteStreams.toByteArray(IndexPageServlet.class.getResourceAsStream("login.html")));
      template = new HtmlTemplate(page);
      template.put("error", "");
    } catch (IOException e) {
      e.printStackTrace();
    }
    servletResponseWriter = new HtmlServletResponseWriter();
  }

  @Ignore
  @SuppressWarnings("unused")
  public LoginPageServlet() {
  }

  public LoginPageServlet(AccountRepository repository, Template template, ServletResponseWriter servletResponseWriter) {
    this.repository = repository;
    this.template = template;
    this.servletResponseWriter = servletResponseWriter;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    servletResponseWriter.writeResponse(resp, template.evaluate());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String pswd = req.getParameter("password");
    Optional<Account> possibleAccount = repository.getByName(name);

    if (!possibleAccount.isPresent()) {
      template.put("error", "Wrong username");
      servletResponseWriter.writeResponse(resp, template.evaluate());
      return;
    }

    Account account = possibleAccount.get();

    if (pswd.equals(account.password)) {
      resp.sendRedirect("/account");
      return;
    } else {
      template.put("error", "Wrong password");
    }

    servletResponseWriter.writeResponse(resp, template.evaluate());
  }

}
