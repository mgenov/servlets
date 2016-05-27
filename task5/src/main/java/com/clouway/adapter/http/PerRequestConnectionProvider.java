package com.clouway.adapter.http;

import com.clouway.core.ConnectionProvider;

import java.sql.Connection;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 25.05.16.
 */
public class PerRequestConnectionProvider implements ConnectionProvider {
  public Connection get() {
    return ConnectionFilter.getCurrentConnection();
  }
}
