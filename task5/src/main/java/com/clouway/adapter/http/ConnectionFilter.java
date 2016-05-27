package com.clouway.adapter.http;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 25.05.16.
 */
public class ConnectionFilter implements Filter {
  private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
  private MysqlConnectionPoolDataSource dataSource;

  public void init(FilterConfig filterConfig) throws ServletException {
    // Singleton, so this code will be executed once.
    dataSource = new MysqlConnectionPoolDataSource();
    dataSource.setURL("jdbc:mysql://localhost:3306/bank");
    dataSource.setUser("root");
    dataSource.setPassword("clouway.com");
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    try {
      Connection threadedConnection = dataSource.getConnection();
      connections.set(threadedConnection);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    // pass
    filterChain.doFilter(servletRequest, servletResponse);

    Connection existing = connections.get();

    // put connection back to the pool, so it could be re-used from other clients
    if (existing != null) {
      try {
        existing.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

  public void destroy() {

  }

  public static Connection getCurrentConnection() {
    return connections.get();
  }
}
