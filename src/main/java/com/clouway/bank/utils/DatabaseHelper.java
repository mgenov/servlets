package com.clouway.bank.utils;

import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.RowGetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DatabaseHelper<T> {
  private final Provider<Connection> provider;

  public DatabaseHelper(Provider<Connection> provider) {
    this.provider = provider;
  }

  public void executeQuery(String query, Object... objects) {
    int index = 0;

    try (PreparedStatement preparedStatement = provider.get().prepareStatement(query)) {

      for (Object object : objects) {
        preparedStatement.setObject(++index, object);
      }

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }

  public T fetchRow(String query, RowGetter getter) {
    try (PreparedStatement preparedStatement = provider.get().prepareStatement(query)) {

      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();

      return (T) getter.getRows(resultSet);
    } catch (SQLException e) {
      throw new ConnectionException("");
    }
  }
}

