package com.clouway.bank.adapter.jdbc;

import com.clouway.bank.core.Provider;
import com.clouway.bank.http.ConnectionFilter;

import java.sql.Connection;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ConnectionProvider implements Provider {
  @Override
  public Connection get() {
    return ConnectionFilter.getConnection();
  }
}
