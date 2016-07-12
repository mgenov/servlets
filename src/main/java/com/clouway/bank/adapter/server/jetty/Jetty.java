package com.clouway.bank.adapter.server.jetty;

import com.clouway.bank.adapter.http.*;
import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentSessionRepository;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentUserRepository;
import com.clouway.bank.core.Provider;
import com.clouway.bank.utils.SessionIdGenerator;
import com.clouway.bank.utils.Timeout;
import com.clouway.bank.validator.UserValidator;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Jetty {
  private final Server server;

  public Jetty(int port) {
    this.server = new Server(port);
  }

  public void start() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addEventListener(new ServletContextListener() {

      public void contextInitialized(final ServletContextEvent servletContextEvent) {
        ConnectionProvider connectionProvider = new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com");

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.addFilter("security", new SecurityFilter(new PersistentSessionRepository(connectionProvider), new Timeout(1))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/home", "/login", "/account");
        servletContext.addServlet("register", new RegisterServlet(new PersistentUserRepository(connectionProvider), new UserValidator())).addMapping("/register");
        servletContext.addServlet("login", new LoginPageServlet()).addMapping("/login");
        servletContext.addServlet("loginController", new LoginControllerServlet(new PersistentUserRepository(connectionProvider), new PersistentSessionRepository(connectionProvider), new UserValidator(), new Timeout(1), new SessionIdGenerator())).addMapping("/loginController");
        servletContext.addServlet("home", new HomePageServlet()).addMapping("/home");
        servletContext.addServlet("/account", new Account()).addMapping("/account");
      }

      public void contextDestroyed(ServletContextEvent servletContextEvent) {

      }
    });

    server.setHandler(context);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
