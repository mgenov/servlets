package com.clouway.bank.adapter.server.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

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
