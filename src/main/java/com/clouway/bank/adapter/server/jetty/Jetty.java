package com.clouway.bank.adapter.server.jetty;

import com.clouway.bank.adapter.http.*;
import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentSessionRepository;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentUserRepository;
import com.clouway.bank.utils.IdsGenerator;
import com.clouway.bank.utils.Timeout;
import com.clouway.bank.validator.UserValidator;
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
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.addFilter("loginFilter", new LoginFilter(new PersistentSessionRepository((new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com"))), new Timeout(1))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/login");
        servletContext.addServlet("register", new RegisterServlet(new PersistentUserRepository(new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com")), new UserValidator())).addMapping("/register");
        servletContext.addServlet("login", new LoginPageServlet()).addMapping("/login");
        servletContext.addServlet("loginController", new LoginControllerServlet(new PersistentUserRepository(new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com")), new PersistentSessionRepository(new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com")), new UserValidator(), new Timeout(1), new IdsGenerator())).addMapping("/loginController");
        servletContext.addServlet("home", new HomePageServlet()).addMapping("/home");

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
