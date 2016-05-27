package com.clouway.server;


import com.clouway.adapter.http.HttpServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;


/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 26.05.16.
 */
public class Jetty {

  private final Server server;

  public Jetty(int port) {
    this.server = new Server(port);
  }

  public void start() {
    ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    contextHandler.setContextPath("/");
    contextHandler.addEventListener(new HttpServletContextListener());
    server.setHandler(contextHandler);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
