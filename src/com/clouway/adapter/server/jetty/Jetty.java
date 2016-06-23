package com.clouway.adapter.server.jetty;

import com.clouway.webapp.pages.FirstPageServlet;
import com.clouway.webapp.pages.SecondPageServlet;
import com.clouway.webapp.pages.ServletDisplay;
import com.clouway.webapp.pages.ServletDispatcher;
import com.clouway.webapp.pages.ThirdPageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Jetty {
  private Server server;
  private int port;

  public Jetty(int port) {
    this.server = new Server(port);
  }

  public void start() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addEventListener(new ServletContextListener() {
      @Override
      public void contextInitialized(final ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.addServlet("pages", new ServletDispatcher()).addMapping("/pages");
        servletContext.addServlet("display", new ServletDisplay()).addMapping("/display");
        servletContext.addServlet("first", new FirstPageServlet()).addMapping("/first");
        servletContext.addServlet("second", new SecondPageServlet()).addMapping("/second");
        servletContext.addServlet("third", new ThirdPageServlet()).addMapping("/third");
      }

      @Override
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
