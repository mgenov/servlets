package com.clouway.bank.adapter.http;

import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
import com.clouway.bank.utils.HtmlHelper;
import com.clouway.bank.utils.HtmlTemplate;
import com.clouway.bank.utils.RowBuilder;
import com.clouway.bank.utils.Pager;
import com.clouway.bank.utils.SessionIdFinder;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class TransactionHistoryPageServlet extends HttpServlet {
  private final TransactionRepository transactionRepository;
  private int pageSize;
  private final SessionRepository sessionRepository;
  private final SessionIdFinder sessionIdFinder;

  public TransactionHistoryPageServlet(TransactionRepository transactionRepository, int pageSize, SessionRepository sessionRepository, SessionIdFinder sessionIdFinder) {
    this.transactionRepository = transactionRepository;
    this.pageSize = pageSize;
    this.sessionRepository = sessionRepository;
    this.sessionIdFinder = sessionIdFinder;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    HtmlHelper helper = new HtmlHelper("web/WEB-INF/historyTable.html");

    String html = helper.loadResource();

    HtmlTemplate template = new HtmlTemplate(html);
    template.put("table", "");
    String page = req.getParameter("page");

    int currentPage;

    RowBuilder rowBuilder = new RowBuilder();

    if (Strings.isNullOrEmpty(page)) {
      currentPage = 1;
    } else {
      currentPage = Integer.valueOf(page);
    }

    String sessionId = sessionIdFinder.findSid(req.getCookies());
    String email = sessionRepository.findUserEmailBySid(sessionId);

    Pager pager = new Pager(transactionRepository, pageSize, email);
    List<Transaction> transactions = pager.getPage(currentPage);

    template.put("previous", String.valueOf(pager.getPreviousPageNumber(currentPage)));
    template.put("next", String.valueOf(pager.getNextPageNumber(currentPage)));

    for (Transaction transaction : transactions) {
      rowBuilder.buildRow(new Date(transaction.date), transaction.email, transaction.operation, transaction.processingAmount, transaction.currentAmount);
    }

    template.put("row", rowBuilder.getRows());
    writer.println(template.evaluate());
  }
}
