package com.clouway.bank.adapter.server.jetty;

import com.clouway.bank.http.MyFilter;
import com.clouway.bank.http.MyServlet;
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
        servletContext.addServlet("servlet", new MyServlet()).addMapping("/servlet");
        servletContext.addFilter("filter", new MyFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
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
