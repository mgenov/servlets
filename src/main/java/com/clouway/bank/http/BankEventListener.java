package com.clouway.bank.http;

import com.clouway.bank.persistent.PerRequestConnectionProvider;
import com.clouway.bank.persistent.PersistentAccountRepository;
import com.clouway.bank.validation.BankTransactionValidator;
import com.clouway.utility.BracketsTemplate;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class BankEventListener implements ServletContextListener {

  private String dbName;

  /**
   * Constructor to assign the name that the application will use
   *
   * @param dbName the name of the database
   */
  public BankEventListener(String dbName) {
    this.dbName = dbName;
  }

  /**
   * Loads the resources to the context, like filters and servlets
   *
   * @param servletContextEvent
   */
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();

    servletContext.addFilter("ConnectionFilter", new ConnectionFilter(dbName)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addServlet("DepositServlet", new DepositServlet(new PersistentAccountRepository(new PerRequestConnectionProvider(), new BankTransactionValidator()), new BracketsTemplate())).addMapping("/deposit");
    servletContext.addServlet("WithdrawServlet", new WithdrawServlet(new PersistentAccountRepository(new PerRequestConnectionProvider(), new BankTransactionValidator()), new BracketsTemplate())).addMapping("/withdraw");
  }

  /**
   * called when the context is being destroyed
   *
   * @param servletContextEvent
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
