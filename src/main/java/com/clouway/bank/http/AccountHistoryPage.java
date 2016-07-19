package com.clouway.bank.http;

import com.clouway.bank.core.AccountPager;
import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.SessionProvider;
import com.clouway.utility.HtmlTableCell;
import com.clouway.utility.TableBuilder;
import com.clouway.utility.Template;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@WebServlet(name = "AccountHistoryPage")
public class AccountHistoryPage extends HttpServlet {
  private Template template;
  private TableBuilder tableBuilder;
  private AccountPager accountPager;
  private SessionProvider sessionProvider;

  public AccountHistoryPage(Template template, TableBuilder tableBuilder, AccountPager accountPager, SessionProvider sessionProvider) {
    this.template = template;
    this.tableBuilder = tableBuilder;
    this.accountPager = accountPager;
    this.sessionProvider = sessionProvider;
  }

  @Override
  public void init() throws ServletException {
    template.loadFromFile("web/WEB-INF/pages/AccountHistory.html");
    template.put("previous", "1");
    template.put("next", "2");
    template.put("currentPage", "1");
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String userId = sessionProvider.get().userId;
    String page = request.getParameter("page");
    int requestPage = 0;
    template.put("nextState", "auto");
    template.put("previousState", "auto");

    try {
      requestPage = Integer.parseInt(page) - 1;
    } catch (Exception e) {
      response.sendRedirect("/history?page=1");
      return;
    }

    List<AccountRecord> accountHistory = accountPager.requestPage(userId, requestPage);
    int previousPageNumber = accountPager.getPreviousPageNumber();
    int currentPageNumber = accountPager.getCurrentPageNumber();
    int nextPageNumber = accountPager.getNextPageNumber();


    if (accountHistory.size() == 0 && requestPage > 0) {
      int lastPage = accountPager.countPages(userId);
      response.sendRedirect("/history?page=" + lastPage);
      return;
    }

    buildTable(accountHistory);

    if (previousPageNumber == currentPageNumber) {
      template.put("previousState", "none");
    }

    if (nextPageNumber == currentPageNumber || accountHistory.size() == 0) {
      template.put("nextState", "none");
    }
    template.put("previous", String.valueOf(previousPageNumber + 1));
    template.put("currentPage", String.valueOf(currentPageNumber + 1));
    template.put("next", String.valueOf(nextPageNumber + 1));
    template.put("userId", userId);
    template.put("table", tableBuilder.build());

    PrintWriter writer = response.getWriter();
    response.setContentType("text/html");
    writer.write(template.evaluate());
  }


  private void buildTable(List<AccountRecord> accountHistory) {
    tableBuilder.aNewRow().cell(new HtmlTableCell("Date")).cell(new HtmlTableCell("User"))
            .cell(new HtmlTableCell("Operation")).cell(new HtmlTableCell("Amount"));

    for (AccountRecord accountRecord : accountHistory) {
      tableBuilder.aNewRow().cell(new HtmlTableCell(new Date(accountRecord.date))).cell(new HtmlTableCell(accountRecord.userId))
              .cell(new HtmlTableCell(accountRecord.operation)).cell(new HtmlTableCell(accountRecord.amount));
    }

  }
}
