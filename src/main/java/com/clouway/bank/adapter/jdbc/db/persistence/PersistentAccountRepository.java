package com.clouway.bank.adapter.jdbc.db.persistence;

import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.RowGetter;
import com.clouway.bank.utils.DatabaseHelper;

import java.sql.Connection;
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
    DatabaseHelper databaseHelper = new DatabaseHelper(provider);

    String query = "INSERT into accounts VALUES (?,?)";
    Double balance = account.getBalance();

    databaseHelper.executeQuery(query, account.email, balance);
  }

  @Override
  public Account findByEmail(final String email) {
    DatabaseHelper databaseHelper = new DatabaseHelper(provider);
    String userEmail = "'" + email + "'";

    return (Account) databaseHelper.fetchRow("SELECT * FROM accounts WHERE email=" + userEmail, new RowGetter() {
      @Override
      public Account getRows(ResultSet resultSet) throws SQLException {
        Double balance = resultSet.getDouble("balance");
        return new Account(email, balance);
      }
    });
  }

  @Override
  public void deposit(String email, Double amount) {
    Account account = findByEmail(email);
    account.deposit(amount);

    DatabaseHelper databaseHelper = new DatabaseHelper(provider);
    String query = "UPDATE accounts SET balance=? WHERE email=?";

    databaseHelper.executeQuery(query, account.getBalance(), email);
  }

  @Override
  public void withdraw(String email, Double amount) {
    Account account = findByEmail(email);
    account.withdraw(amount);

    DatabaseHelper databaseHelper = new DatabaseHelper(provider);
    String query = "UPDATE accounts SET balance=? WHERE email=?";

    databaseHelper.executeQuery(query, account.getBalance(), email);
  }
}
