package com.clouway;

import com.clouway.http.server.JettyServer;
import com.google.common.base.Strings;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class App {
  public static void main(String[] args) {

    // User default database when no configuration is specified
    if (Strings.isNullOrEmpty(System.getenv("BANK_DB_HOST"))) {
      System.setProperty("BANK_DB_HOST", "localhost");
      System.setProperty("BANK_DB_USER", "root");
      System.setProperty("BANK_DB_PASS", "123123");
    }
    JettyServer server = new JettyServer(8080);
    server.start();
  }
}