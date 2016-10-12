package main.java.com.clouway.persistent.datastore;

import java.sql.ResultSet;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface RowFetcher<T> {
  T fetchRow(ResultSet resultSet);
}