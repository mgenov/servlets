package com.clouway.http.servlets;

import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.ServletPageRenderer;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.adapter.jdbc.PersistentAccountRepository;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.annotations.VisibleForTesting;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class LoginPageServlet extends HttpServlet {
  private AccountRepository repository;
  private ServletPageRenderer servletResponseWriter;

  @Ignore
  @SuppressWarnings("unused")
  public LoginPageServlet() {
    this(new PersistentAccountRepository(new DataStore(new ConnectionProvider())),new HtmlServletPageRenderer());
  }

  @VisibleForTesting
  public LoginPageServlet(AccountRepository repository, ServletPageRenderer servletResponseWriter) {
    this.repository = repository;
    this.servletResponseWriter = servletResponseWriter;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    servletResponseWriter.renderPage("login.html", Collections.singletonMap("error", ""), resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String pswd = req.getParameter("password");
    Optional<Account> possibleAccount = repository.getByName(name);

    if (!possibleAccount.isPresent()) {
      servletResponseWriter.renderPage("login.html", Collections.singletonMap("error", "Wrong username"), resp);
      return;
    }

    Account account = possibleAccount.get();

    if (pswd.equals(account.password)) {
      resp.sendRedirect("/account");
    } else {
      servletResponseWriter.renderPage("login.html", Collections.singletonMap("error", "Wrong password"), resp);
    }
  }

}
