package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.utility.Template;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for the deposit, displaying the html and handling requests for deposit
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DepositServlet extends HttpServlet {
  private AccountRepository accountRepository;
  private Template template;


  /**
   * Constructor for the deposit setting the AccountRepository and Template
   *
   * @param accountRepository account repository storing the account data
   * @param template          template for manipulating strings
   */
  public DepositServlet(AccountRepository accountRepository, Template template) {
    this.accountRepository = accountRepository;
    this.template = template;
  }

  /**
   * initializes the servlet by setting the html file on the template
   * and hides the errorMessage template tag
   *
   * @throws ServletException
   */
  @Override
  public void init() throws ServletException {
    template.loadFromFile("web/WEB-INF/pages/Deposit.html");
    template.put("username", "no user yet");
    template.put("balance", "not available");
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
    template.put("username", username);
    template.put("balance", balance.toString());
    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");
    writer.write(template.evaluate());
  }

  /**
   * Handling the POST request, about depositing funds
   *
   * @param req  http request
   * @param resp http response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String username = req.getParameter("username");
    String amount = req.getParameter("amount");

    Double depositedAmount = accountRepository.deposit(new Amount(username, amount));
    template.put("username", username);
    template.put("balance", depositedAmount.toString());

    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");
    writer.write(template.evaluate());
  }

}
