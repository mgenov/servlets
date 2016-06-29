package com.clouway.bank.adapter.server.jetty;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.persistence.PersistentUserRepository;
import com.clouway.bank.http.ConnectionFilter;
import com.clouway.bank.http.RegisterServlet;
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
        servletContext.addFilter("connection", new ConnectionFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        servletContext.addServlet("register", new RegisterServlet(new PersistentUserRepository(new ConnectionProvider()))).addMapping("/register");
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
