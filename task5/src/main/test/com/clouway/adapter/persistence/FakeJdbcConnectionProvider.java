package com.clouway.adapter.persistence;

import com.clouway.adapter.http.PerRequestConnectionProvider;
import com.clouway.core.ConnectionProvider;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 03.06.16.
 */
public class FakeJdbcConnectionProvider implements ConnectionProvider {
  private MysqlConnectionPoolDataSource dataSource;

  public Connection get() {
    dataSource = new MysqlConnectionPoolDataSource();
    dataSource.setURL("jdbc:mysql://localhost:3306/bank");
    dataSource.setUser("root");
    dataSource.setPassword("clouway.com");
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
}
