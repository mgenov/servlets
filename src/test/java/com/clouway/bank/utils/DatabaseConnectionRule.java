package com.clouway.bank.utils;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DatabaseConnectionRule implements TestRule {
  private MysqlConnectionPoolDataSource connectionPool;

  public DatabaseConnectionRule(String dbName) {
    connectionPool = new MysqlConnectionPoolDataSource();
    connectionPool.setURL("jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    connectionPool.setUser("root");
    connectionPool.setPassword("clouway.com");
  }

  @Override
  public Statement apply(Statement statement, Description description) {

    return statement;
  }

  public Connection getConnection() {
    try {
      return connectionPool.getConnection();
    } catch (SQLException e) {
      return null;
    }
  }
}
