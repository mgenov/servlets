package com.clouway.bank.persistent;

import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DatabaseHelper implements DataStore {
  private ConnectionProvider connectionProvider;

  public DatabaseHelper(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  @Override
  public <T> List<T> fetchRows(String query, RowFetcher<T> fetcher, Object... params) {
    Connection connection = connectionProvider.get();
    List<T> result = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      int index = 0;
      for (Object obj : params) {
        preparedStatement.setObject(++index, obj);
      }

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        result.add(fetcher.fetchRow(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public void executeQuery(String query, Object... params) {
    Connection connection = connectionProvider.get();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      int index = 0;
      for (Object obj : params) {
        preparedStatement.setObject(++index, obj);
      }

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
