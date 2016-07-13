package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.CurrentTime;
import com.clouway.bank.core.Date;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.TransactionHistory;
import com.clouway.bank.utils.Timeout;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentAccountRepository implements AccountRepository {
  private final Provider<Connection> provider;
  private final Date date;

  public PersistentAccountRepository(Provider<Connection> provider, Date date) {
    this.provider = provider;
    this.date = date;
  }

  @Override
  public void createAccount(Account account) {
    Double balance = account.getBalance();
    try (PreparedStatement statement = provider.get().prepareStatement("INSERT into accounts VALUES (?,?)")) {
      statement.setString(1, account.email);
      statement.setDouble(2, balance);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public Account findByEmail(String email) {
    try (PreparedStatement statement = provider.get().prepareStatement("SELECT * FROM accounts WHERE email=?")) {
      statement.setString(1, email);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Double balance = resultSet.getDouble("balance");

        return new Account(email, balance);
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return null;
  }

  @Override
  public void deposit(String email, Double cash) {
    Account account = findByEmail(email);
    account.deposit(cash);

    try (PreparedStatement statement = provider.get().prepareStatement("UPDATE accounts SET balance=? WHERE email=?")) {
      statement.setDouble(1, account.getBalance());
      statement.setString(2, email);

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public void withdraw(String email, Double cash) {
    Account account = findByEmail(email);
    account.withdraw(cash);

    Double balance = account.getBalance();

    try (PreparedStatement statement = provider.get().prepareStatement("UPDATE accounts SET balance=? WHERE email=?")) {
      statement.setDouble(1, balance);
      statement.setString(2, email);

      statement.executeUpdate();

    } catch (SQLException e) {
      throw new ConnectionException("Can not connect to database");
    }
  }

  @Override
  public Double getBalance(String email) {
    try (PreparedStatement statement = provider.get().prepareStatement("SELECT balance FROM accounts WHERE email=?")) {
      statement.setString(1, email);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        return resultSet.getDouble("balance");
      }
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
    return null;
  }

  @Override
  public void updateHistory(String email, String operation) {
    try (PreparedStatement statement = provider.get().prepareStatement("INSERT INTO transactions VALUES (?,?,?,?)")) {
      statement.setLong(1, date.getCurrentDate());
      statement.setString(2, email);
      statement.setString(3, operation);
      statement.setDouble(4, getBalance(email));

      statement.executeUpdate();
    } catch (SQLException e) {
      new ConnectionException("Cannot connect to database");
    }
  }

  @Override
  public List<TransactionHistory> getTransactionsHistory() {
    TransactionHistory transaction = null;
    try (PreparedStatement statement = provider.get().prepareStatement("SELECT * FROM transactions")) {
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        String email = resultSet.getString("email");
        String operation = resultSet.getString("operation");
        Double balance = resultSet.getDouble("currentBalance");
        transaction = new TransactionHistory(date.getCurrentDate(), email, operation, balance);
      }

      return Lists.newArrayList(transaction);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ConnectionException("Cannot connect to database");
    }
  }
}
