package com.clouway.bank.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Jetty {
  private final String dbName;
  private Server server;

  /**
   * Jetty constructor to set up the servers port
   * and the database that the application uses
   *
   * @param port   the port that the server uses
   * @param dbName the name of the database the application uses
   */
  public Jetty(int port, String dbName) {
    this.dbName = dbName;
    this.server = new Server(port);
  }

  /**
   * Starts the server
   */
  public synchronized void start() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addEventListener(new BankEventListener(dbName));

    server.setHandler(context);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * stops the server
   */
  public void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
