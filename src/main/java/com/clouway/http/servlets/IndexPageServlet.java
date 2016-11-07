package com.clouway.http.servlets;

import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.ServletPageRenderer;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.adapter.jdbc.PersistentAccountRepository;
import com.clouway.persistent.datastore.DataStore;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class IndexPageServlet extends HttpServlet {
  private AccountRepository repository;
  private ServletPageRenderer servletResponseWriter;

  @Override
  public void init() throws ServletException {
    ConnectionProvider provider = new ConnectionProvider();
    DataStore dataStore = new DataStore(provider);
    repository = new PersistentAccountRepository(dataStore);
    servletResponseWriter = new HtmlServletPageRenderer();
  }

  @Ignore
  @SuppressWarnings("unused")
  public IndexPageServlet() {
  }

  public IndexPageServlet(AccountRepository repository, ServletPageRenderer servletResponseWriter) {
    this.repository = repository;
    this.servletResponseWriter = servletResponseWriter;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    servletResponseWriter.renderPage("index.html", Collections.emptyMap(), resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");

    if (!repository.getByName(name).isPresent()) {
      String password = req.getParameter("password");
      Account account = new Account(name, password, 0);

      repository.register(account);

      resp.sendRedirect("/login");
    } else {
      servletResponseWriter.renderPage("index.html", Collections.singletonMap("error", "Username is taken"), resp);
    }
  }
}