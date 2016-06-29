package com.clouway.bank.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
@WebFilter("connection")
public class ConnectionFilter implements Filter {
  private static ThreadLocal<Connection> connections = new ThreadLocal<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    try {
      connections.set(DriverManager.getConnection("jdbc:postgresql://localhost/bank", "postgres", "clouway.com"));
      filterChain.doFilter(servletRequest, servletResponse);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void destroy() {

  }

  public static Connection getConnection() {
    return connections.get();
  }
}
