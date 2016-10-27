package com.clouway.http.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class JettyServer {
  private final int port;

  private WebAppContext context = new WebAppContext();

  public JettyServer(int port) {
    this.port = port;
  }

  public void start() {
    Server server = new Server(port);

    context.setResourceBase(".");
    context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
    context.setResourceBase("../test-jetty-webapp/src/main/webapp");
    context.setContextPath("/");

    server.setHandler(context);
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}