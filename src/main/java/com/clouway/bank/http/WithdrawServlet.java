package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.TransactionValidator;
import com.clouway.utility.Template;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class WithdrawServlet extends HttpServlet {
  private final AccountRepository accountRepository;
  private final Template template;
  private TransactionValidator validator;

  public WithdrawServlet(AccountRepository accountRepository, Template template, TransactionValidator validator) {
    this.accountRepository = accountRepository;
    this.template = template;
    this.validator = validator;
  }

  /**
   * initializes the servlet by setting the html file on the template
   * and hides the errorMessage template tag
   *
   * @throws ServletException
   */
  @Override
  public void init() throws ServletException {
    template.loadFromFile("web/WEB-INF/pages/Withdraw.html");
    template.put("message", "");
  }

  /**
   * Handling GET requests, displaying the html file
   *
   * @param req  http request
   * @param resp http response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String username = "Stanislava";
    Double balance = accountRepository.getCurrentBalance(username);
    String message = req.getParameter("message");

    template.put("username", username);
    template.put("balance", balance.toString());
    if (message != null) {
      template.put("message", message);
    }

    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");
    writer.write(template.evaluate());
  }

  /**
   * Handling the POST request, about withdrawing funds
   *
   * @param req  http request
   * @param resp http response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String username = req.getParameter("username");
    String amountParam = req.getParameter("amount");
    if (amountParam == null || amountParam.isEmpty()) {
      resp.sendRedirect("withdraw?message=enter amount");
      return;
    }
    String validationMessage = validator.validateAmount(amountParam);

    if (!validationMessage.isEmpty()) {
      resp.sendRedirect("withdraw?message=" + validationMessage);
      return;
    }

    Double amount = Double.parseDouble(amountParam);
    accountRepository.withdraw(new Amount(username, amount));

    resp.sendRedirect("withdraw?message=Susscesful deposit: " + amount);
  }

}
