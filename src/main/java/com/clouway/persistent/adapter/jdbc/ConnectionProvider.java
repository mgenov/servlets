package main.java.com.clouway.persistent.adapter.jdbc;

import main.java.com.clouway.core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {
  private final String db;

  public ConnectionProvider(String db) {
    this.db = db;
  }

  @Override
  public Connection get() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?autoReconnect=true&useSSL=false", "root", "123");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}