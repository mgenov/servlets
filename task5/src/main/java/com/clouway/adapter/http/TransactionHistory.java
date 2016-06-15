package com.clouway.adapter.http;

import com.clouway.core.FundsRepository;
import com.clouway.core.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 13.06.16.
 */
@WebServlet(name = "TransactionHistory")
public class TransactionHistory extends HttpServlet {
  private FundsRepository fundsRepository;
  public static final int PAGE_SIZE = 20;

  public TransactionHistory(FundsRepository fundsRepository) {
    this.fundsRepository = fundsRepository;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    boolean hasNext = false;
    List<Transaction> transactions;
    Integer currentPage;
    if (request.getParameter("page") == null) {
      currentPage = 1;
    } else {
      currentPage = Integer.valueOf(request.getParameter("page"));
    }


    if (currentPage == null) {
      currentPage = 1;
    }

    if (currentPage == 1) {
      transactions = fundsRepository.getHistory(PAGE_SIZE + 1, 0);
    } else {
      transactions = fundsRepository.getHistory(PAGE_SIZE + 1, (currentPage - 1) * PAGE_SIZE);
    }

    if (PAGE_SIZE < transactions.size()) {
      hasNext = true;
      transactions.remove(PAGE_SIZE);
    }

    out.print("<table width=25% border=1>");
    out.print("<center><h1>Transaction History:</h1></center>");
    out.print("<tr>");
    out.print("<th> ID </th>");
    out.print("<th> Date </th>");
    out.print("<th> E-mail </th>");
    out.print("<th> Operation </th>");
    out.print("<th> Amount</th>");
    out.print("</tr>");
    for (Transaction transaction : transactions) {
      out.print("<tr>");
      out.print("<td>" + transaction.ID + "</td>");
      out.print("<td>" + transaction.date + "</td>");
      out.print("<td>" + transaction.email + "</td>");
      out.print("<td>" + transaction.operation + "</td>");
      out.print("<td>" + transaction.amount + "</td>");
      out.print("</tr>");
    }
    out.print("</table>");
    out.println("<form action=\"/history\" method=\"get\">");

    if (currentPage - 1 > 0) {
      Integer previousPage = currentPage - 1;
      out.println("<a style=\"text-align:left\" href=\"/history?page=" + previousPage + "\">previous</a>");
    }

    if (hasNext) {
      Integer nextPage = currentPage + 1;
      out.println("<a style=\"margin:330px;\" href=\"/history?page=" + nextPage + "\">next</a>");
    }

    out.println("</form>");
    out.print("<a href=\"/useraccount\">Back</a>");
    out.flush();
    out.close();
  }
}
