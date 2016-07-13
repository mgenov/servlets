package com.clouway.bank.adapter.http;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;
import com.clouway.bank.utils.SessionIdFinder;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AccountPageServlet extends HttpServlet {
  private final SessionRepository sessionRepository;
  private final AccountRepository accountRepository;
  private final SessionIdFinder sessionIdFinder;

  public AccountPageServlet(SessionRepository repository, AccountRepository accountRepository, SessionIdFinder sessionIdFinder) {
    this.sessionRepository = repository;
    this.accountRepository = accountRepository;
    this.sessionIdFinder = sessionIdFinder;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/account.html");
    String page = helper.loadResource();

    HtmlTemplate template = new HtmlTemplate(page);
    template.put("email", "");
    template.put("balance", "");
    template.put("message", "");

    String errors = req.getParameter("errorMessage");
    String sessionId = sessionIdFinder.findSid(req.getCookies());

    render(template, errors, sessionId);

    PrintWriter writer = resp.getWriter();
    writer.println(template.evaluate());

    writer.flush();
  }

  private void render(HtmlTemplate template, String errors, String sessionId) {
    if (errors != null) {
      template.put("message", errors);
      return;
    }

    String email = sessionRepository.findUserEmailBySid(sessionId);
    Account account = accountRepository.findByEmail(email);

    template.put("email", email);

    Double balance = account.getBalance();
    template.put("balance", String.valueOf(balance));
  }
}
