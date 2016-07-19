package com.clouway.bank.http;

import com.clouway.bank.core.AccountHistoryPager;
import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.CurrentSessionProvider;
import com.clouway.bank.core.DataStore;
import com.clouway.bank.http.validation.BankTransactionValidator;
import com.clouway.bank.http.validation.UserDataValidator;
import com.clouway.bank.persistent.DatabaseHelper;
import com.clouway.bank.persistent.PerRequestConnectionProvider;
import com.clouway.bank.persistent.PersistentAccountHistoryRepository;
import com.clouway.bank.persistent.PersistentAccountRepository;
import com.clouway.bank.persistent.PersistentSessionRepository;
import com.clouway.bank.persistent.PersistentUserRepository;
import com.clouway.utility.BracketsTemplate;
import com.clouway.utility.FileReader;
import com.clouway.utility.HtmlTableBuilder;

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
    int sessionDuration = 5;
    BankTimeCalendar calendar = new BankTimeCalendar(Calendar.getInstance(), sessionDuration);

    accessible.add("/login");
    accessible.add("/logincontroller");
    accessible.add("/registercontroller");
    accessible.add("/register");

    CurrentSessionProvider currentSessionProvider = new CurrentSessionProvider();

    ConnectionProvider connectionProvider = new PerRequestConnectionProvider();
    DataStore dataStore = new DatabaseHelper(connectionProvider);

    servletContext.addFilter("ConnectionFilter", new ConnectionFilter(dbName)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addFilter("HttprequestErrorReporter", new HttprequestErrorReporter(new BracketsTemplate(new FileReader()))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addFilter("SecurityFilter", new SecurityFilter(new PersistentSessionRepository(dataStore), calendar, accessible, currentSessionProvider)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addServlet("LoginPage", new LoginPage(new BracketsTemplate(new FileReader()))).addMapping("/login");
    servletContext.addServlet("LoginController", new LoginController(new PersistentSessionRepository(dataStore), new PersistentUserRepository(dataStore), new UserDataValidator(), calendar)).addMapping("/logincontroller");
    servletContext.addServlet("HomePage", new HomePage(new BracketsTemplate(new FileReader()), new PersistentSessionRepository(dataStore), calendar)).addMapping("");
    servletContext.addServlet("RegistrationPage", new RegistrationPage(new BracketsTemplate(new FileReader()))).addMapping("/register");
    servletContext.addServlet("RegisterControllerServlet", new RegistrationController(new PersistentUserRepository(dataStore), new PersistentAccountRepository(dataStore), new UserDataValidator())).addMapping("/registercontroller");
    servletContext.addServlet("DepositServlet", new DepositServlet(new PersistentAccountRepository(dataStore), new BracketsTemplate(new FileReader()), new BankTransactionValidator(), currentSessionProvider)).addMapping("/deposit");
    servletContext.addServlet("WithdrawServlet", new WithdrawServlet(new PersistentAccountRepository(dataStore), new BracketsTemplate(new FileReader()), new BankTransactionValidator(), currentSessionProvider)).addMapping("/withdraw");
    servletContext.addServlet("LogoutController", new LogoutController(new PersistentSessionRepository(dataStore), currentSessionProvider)).addMapping("/logoutcontroller");
    servletContext.addServlet("AccountHistory", new AccountHistoryPage(new BracketsTemplate(new FileReader()), new HtmlTableBuilder("table table-hover table-bordered"), new AccountHistoryPager(20, new PersistentAccountHistoryRepository(dataStore)), currentSessionProvider)).addMapping("/history");
  }

  /**
   * called when the context is being destroyed
   *
   * @param servletContextEvent
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
