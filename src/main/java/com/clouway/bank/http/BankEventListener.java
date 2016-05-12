package com.clouway.bank.http;

import com.clouway.bank.core.CurrentSessionProvider;
import com.clouway.bank.http.validation.BankTransactionValidator;
import com.clouway.bank.http.validation.UserDataValidator;
import com.clouway.bank.persistent.PerRequestConnectionProvider;
import com.clouway.bank.persistent.PersistentAccountRepository;
import com.clouway.bank.persistent.PersistentSessionRepository;
import com.clouway.bank.persistent.PersistentUserRepository;
import com.clouway.utility.BracketsTemplate;
import com.clouway.utility.FileReader;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

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
    List<String> accessible = new ArrayList<>();

    accessible.add("/login");
    accessible.add("/logincontroller");
    accessible.add("/registercontroller");
    accessible.add("/register");

    CurrentSessionProvider currentSessionProvider = new CurrentSessionProvider();

    servletContext.addFilter("ConnectionFilter", new ConnectionFilter(dbName)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addFilter("HttprequestErrorReporter", new HttprequestErrorReporter(new BracketsTemplate(new FileReader()))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addFilter("SecurityFilter", new SecurityFilter(new PersistentSessionRepository(new PerRequestConnectionProvider()), new BankTimeCalendar(Calendar.getInstance(), 5), accessible, currentSessionProvider)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addServlet("LoginPage", new LoginPage(new BracketsTemplate(new FileReader()))).addMapping("/login");
    servletContext.addServlet("LoginController", new LoginController(new PersistentSessionRepository(new PerRequestConnectionProvider()), new PersistentUserRepository(new PerRequestConnectionProvider()), new UserDataValidator(), new BankTimeCalendar(Calendar.getInstance(), 5))).addMapping("/logincontroller");
    servletContext.addServlet("HomePage", new HomePage(new BracketsTemplate(new FileReader()))).addMapping("");
    servletContext.addServlet("RegistrationPage", new RegistrationPage(new BracketsTemplate(new FileReader()))).addMapping("/register");
    servletContext.addServlet("RegisterControllerServlet", new RegistrationController(new PersistentUserRepository(new PerRequestConnectionProvider()), new PersistentAccountRepository(new PerRequestConnectionProvider()), new UserDataValidator())).addMapping("/registercontroller");
    servletContext.addServlet("DepositServlet", new DepositServlet(new PersistentAccountRepository(new PerRequestConnectionProvider()), new BracketsTemplate(new FileReader()), new BankTransactionValidator(), currentSessionProvider)).addMapping("/deposit");
    servletContext.addServlet("WithdrawServlet", new WithdrawServlet(new PersistentAccountRepository(new PerRequestConnectionProvider()), new BracketsTemplate(new FileReader()), new BankTransactionValidator(), currentSessionProvider)).addMapping("/withdraw");
  }

  /**
   * called when the context is being destroyed
   *
   * @param servletContextEvent
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
