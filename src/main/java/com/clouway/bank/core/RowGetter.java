package com.clouway.bank.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface RowGetter<T> {
  T getRows(ResultSet resultSet) throws SQLException;
}
