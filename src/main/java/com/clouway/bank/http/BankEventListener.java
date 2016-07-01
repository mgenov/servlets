package com.clouway.bank.http;

import com.clouway.bank.persistent.PerRequestConnectionProvider;
import com.clouway.bank.persistent.PersistentAccountRepository;
import com.clouway.bank.persistent.PersistentUserRepository;
import com.clouway.bank.validation.BankTransactionValidator;
import com.clouway.bank.validation.UserDataValidator;
import com.clouway.utility.BracketsTemplate;
import com.clouway.utility.FileReader;

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
    servletContext.addFilter("HttprequestErrorReporter", new HttprequestErrorReporter(new BracketsTemplate(new FileReader()))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addServlet("RegistrationPage", new RegistrationPage(new BracketsTemplate(new FileReader()))).addMapping("/register");
    servletContext.addServlet("RegisterControllerServlet", new RegistrationController(new PersistentUserRepository(new PerRequestConnectionProvider(), new UserDataValidator()), new PersistentAccountRepository(new PerRequestConnectionProvider(), new BankTransactionValidator()))).addMapping("/registercontroller");
    servletContext.addServlet("DepositServlet", new DepositServlet(new PersistentAccountRepository(new PerRequestConnectionProvider(), new BankTransactionValidator()), new BracketsTemplate(new FileReader()))).addMapping("/deposit");
    servletContext.addServlet("WithdrawServlet", new WithdrawServlet(new PersistentAccountRepository(new PerRequestConnectionProvider(), new BankTransactionValidator()), new BracketsTemplate(new FileReader()))).addMapping("/withdraw");
  }

  /**
   * called when the context is being destroyed
   *
   * @param servletContextEvent
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
