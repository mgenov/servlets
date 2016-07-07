package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountHistoryRepository;
import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.ConnectionProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentAccountHistoryRepository implements AccountHistoryRepository {
  private ConnectionProvider connectionProvider;

  public PersistentAccountHistoryRepository(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  public int countRecords(String userId) {
    String query = "SELECT COUNT(*) FROM account_history WHERE username = ?;";

    try {
      Connection connection = connectionProvider.get();
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return resultSet.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public List<AccountRecord> getAccountRecords(String userId, int offset, int limit) {
    String query = "SELECT date, username, operation, amount FROM account_history WHERE username=? ORDER BY date LIMIT ? OFFSET ?;";
    List<AccountRecord> accountHistory = new ArrayList<>();
    try {
      Connection connection = connectionProvider.get();
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, userId);
      preparedStatement.setInt(2, limit);
      preparedStatement.setInt(3, offset);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Long date = resultSet.getTimestamp(1).getTime();
        String name = resultSet.getString(2);
        String operation = resultSet.getString(3);
        Double amount = round(resultSet.getDouble(4), 2);
        accountHistory.add(new AccountRecord(date, name, operation, amount));
      }
      resultSet.close();
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return accountHistory;
  }

  private double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
