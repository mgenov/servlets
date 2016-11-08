package com.clouway.core;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class ConnectionPool {
  private static BasicDataSource source = null;

  public static synchronized BasicDataSource get() {
    if (source == null) {
      source = new BasicDataSource();
      String host = System.getenv("BANK_DB_HOST");
      String user = System.getenv("BANK_DB_USER");
      String pass = System.getenv("BANK_DB_PASS");

      source.setDriverClassName("com.mysql.jdbc.Driver");
      source.setUsername(user);
      source.setPassword(pass);
      source.setUrl("jdbc:mysql://" + host + "/myBank" + "?autoReconnect=true&useSSL=false");
      source.setInitialSize(3);
    }
    return source;
  }
}
