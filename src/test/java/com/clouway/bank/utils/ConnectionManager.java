package com.clouway.bank.utils;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ConnectionManager {
  private MysqlConnectionPoolDataSource connectionPool;

  public ConnectionManager() {
    connectionPool = new MysqlConnectionPoolDataSource();
    connectionPool.setURL("jdbc:mysql://localhost:3306/bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    connectionPool.setUser("root");
    connectionPool.setPassword("clouway.com");
  }

  public Connection getConnection() throws SQLException {
    return connectionPool.getConnection();
  }
}
