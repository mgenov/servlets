package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ConnectionManager implements ConnectionProvider {

  private final MysqlConnectionPoolDataSource connectionPool;

  public ConnectionManager(String dbName, String user, String password) {
    connectionPool = new MysqlConnectionPoolDataSource();
    connectionPool.setURL("jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    connectionPool.setUser(user);
    connectionPool.setPassword(password);
  }

  @Override
  public Connection get() {
    try {
      return connectionPool.getConnection();
    } catch (SQLException e) {
      return null;
    }
  }
}
