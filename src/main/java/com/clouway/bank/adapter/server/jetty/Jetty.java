package com.clouway.bank.adapter.server.jetty;

import com.clouway.bank.adapter.http.HomePageServlet;
import com.clouway.bank.adapter.http.LoginControllerServlet;
import com.clouway.bank.adapter.http.LoginPageServlet;
import com.clouway.bank.adapter.http.RegisterServlet;
import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentUserRepository;
import com.clouway.bank.validator.UserValidator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
        servletContext.addServlet("register", new RegisterServlet(new PersistentUserRepository(new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com")), new UserValidator())).addMapping("/register");
        servletContext.addServlet("login", new LoginPageServlet()).addMapping("/login");
        servletContext.addServlet("loginController", new LoginControllerServlet(new PersistentUserRepository(new ConnectionProvider("jdbc:postgresql://localhost/bank", "postgres", "clouway.com")), new UserValidator())).addMapping("/loginController");
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
