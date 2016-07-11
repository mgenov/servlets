package com.clouway.bank.adapter.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

  public AccountPageServlet(SessionRepository repository, AccountRepository accountRepository) {
    this.sessionRepository = repository;
    this.accountRepository = accountRepository;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/account.html");
    String page = helper.loadResource();

    HtmlTemplate template = new HtmlTemplate(page);
    template.put("email", "");
    template.put("amount", "");
    template.put("message", "");

    String errors = req.getParameter("errorMessage");

    Cookie[] cookies = req.getCookies();
    Cookie cookie = find(cookies);
    render(template, errors, cookie);

    PrintWriter writer = resp.getWriter();
    writer.println(template.evaluate());

    writer.flush();
  }

  private void render(HtmlTemplate template, String errors, Cookie cookie) {
    String email;
    if (errors != null) {
      template.put("message", errors);
    }
    if (cookie != null) {
      email = sessionRepository.findEmailById(cookie.getValue());
      template.put("email", email);

      Double balance = accountRepository.getBalance(email);
      template.put("balance", String.valueOf(balance));
    }
  }

  private Cookie find(Cookie[] cookies) {
    for (Cookie each : cookies) {
      if (each.getName().equals("id")) {
        return each;
      }
    }
    return null;
  }
}
