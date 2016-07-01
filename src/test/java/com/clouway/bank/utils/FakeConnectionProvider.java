package com.clouway.bank.utils;

import com.clouway.bank.core.ConnectionProvider;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class FakeConnectionProvider implements ConnectionProvider {
  private MysqlConnectionPoolDataSource connectionPool;

  public FakeConnectionProvider() {
    connectionPool = new MysqlConnectionPoolDataSource();
    connectionPool.setURL("jdbc:mysql://localhost:3306/bank_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    connectionPool.setUser("root");
    connectionPool.setPassword("clouway.com");
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
