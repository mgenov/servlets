package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentAccountRepository implements AccountRepository {
  private final Provider<Connection> provider;

  public PersistentAccountRepository(Provider<Connection> provider) {
    this.provider = provider;
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
      resultSet.next();

      return new Account(email, resultSet.getDouble("balance"));
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
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
      resultSet.next();

      return resultSet.getDouble("balance");
    } catch (SQLException e) {
      throw new ConnectionException("Cannot connect to database");
    }
  }
}