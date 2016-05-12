package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.SessionProvider;
import com.clouway.bank.core.TransactionValidator;
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
  private TransactionValidator validator;
  private SessionProvider sessionProvider;


  /**
   * Constructor for the deposit setting the AccountRepository and Template
   *
   * @param accountRepository account repository storing the account data
   * @param template          template for manipulating strings
   */
  public DepositServlet(AccountRepository accountRepository, Template template, TransactionValidator validator, SessionProvider sessionProvider) {
    this.accountRepository = accountRepository;
    this.template = template;
    this.validator = validator;
    this.sessionProvider = sessionProvider;
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
    String username = sessionProvider.get().userId;
    Double balance = accountRepository.getCurrentBalance(username);
    String message = req.getParameter("message");
    template.put("userId", username);
    template.put("balance", balance.toString());
    if (message != null) {
      template.put("message", message);
    } else {
      template.put("message", "");
    }

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
    String username = req.getParameter("userId");
    String amountParam = req.getParameter("amount");
    if (amountParam == null || amountParam.isEmpty()) {
      resp.sendRedirect("deposit?message=enter amount");
      return;
    }
    String validationMessage = validator.validateAmount(amountParam);

    if (!validationMessage.isEmpty()) {
      resp.sendRedirect("deposit?message=" + validationMessage);
      return;
    }

    Double amount = Double.parseDouble(amountParam);
    accountRepository.deposit(new Amount(username, amount));

    resp.sendRedirect("deposit?message=Successful deposit: " + amount);
  }

}
