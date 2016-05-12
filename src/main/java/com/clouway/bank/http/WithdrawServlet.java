package com.clouway.bank.http;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.utility.Template;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class WithdrawServlet extends HttpServlet {
  private final AccountRepository accountRepository;
  private final Template template;

  public WithdrawServlet(AccountRepository accountRepository, Template template) {
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
    template.setTemplate(getHtml("web/WEB-INF/pages/Withdraw.html"));
    template.put("errorMessage", "");
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
    Double balance = null;
    try {
      balance = accountRepository.getCurrentBalance(username);
    } catch (ValidationException e) {
      balance = 0d;
    }

    template.put("username", username);
    template.put("balance", balance.toString());
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
    Double balance = null;
    String amount = req.getParameter("amount");
    try {
      balance = accountRepository.withdraw(new Amount(username, amount));
      template.put("errorMessage", "");
      template.put("username", username);
      template.put("balance", balance.toString());
    } catch (ValidationException e) {
      template.put("errorMessage", e.getMessage() + "<br>");
    }

    PrintWriter writer = resp.getWriter();
    resp.setContentType("text/html");
    writer.write(template.evaluate());
  }

  /**
   * Loading file as string
   *
   * @param filePath the path to the file
   * @return the string read from the file
   */
  private String getHtml(String filePath) {
    File file = new File(filePath);
    URL url = null;
    try {
      url = file.toURI().toURL();
      InputStream in = url.openStream();

      return IOUtils.toString(in);

    } catch (IOException e) {
      return "404 Page not found!";
    }

  }
}
