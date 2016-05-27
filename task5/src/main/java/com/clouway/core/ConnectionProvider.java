package com.clouway.core;

import java.sql.Connection;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 25.05.16.
 */
public interface ConnectionProvider {

  Connection get();
}
