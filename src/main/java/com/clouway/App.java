package com.clouway;

import com.clouway.http.server.JettyServer;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class App {
  public static void main(String[] args) {
    JettyServer server = new JettyServer(8080);
    server.start();
  }
}