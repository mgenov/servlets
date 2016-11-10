package com.clouway.http.servlets;

import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.RegexValidator;
import com.clouway.core.ServletPageRenderer;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.adapter.jdbc.PersistentAccountRepository;
import com.clouway.persistent.datastore.DataStore;
import com.google.common.annotations.VisibleForTesting;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
    this(
            new PersistentAccountRepository(new DataStore(new ConnectionProvider())),
            new HtmlServletPageRenderer()
    );
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
    RegexValidator nameValidator = new RegexValidator("[a-zA-Z]{1,50}");

    if (!possibleAccount.isPresent() || !nameValidator.check(name)) {
      servletResponseWriter.renderPage("login.html", Collections.singletonMap("error", "Wrong username"), resp);
      return;
    }

    Account account = possibleAccount.get();

    RegexValidator pswdValidator = new RegexValidator("[a-zA-Z_0-9]{6,18}");


    if (!pswd.equals(account.password) || !pswdValidator.check(pswd)) {
      servletResponseWriter.renderPage("login.html", Collections.singletonMap("error", "Wrong password"), resp);
    } else {
      resp.addCookie(new Cookie("SID", "123"));
      resp.sendRedirect("/");
    }
  }
}
