package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.TransactionValidator;
import com.clouway.bank.core.UserException;
import com.clouway.bank.core.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account data storing
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentAccountRepository implements AccountRepository {
  private ConnectionProvider connectionProvider;
  private TransactionValidator validator;

  /**
   * constructor setting the ConnectionProvider and the TransactionValidator
   *
   * @param connectionProvider ConnectionProvider
   * @param validator          TransactionValidator
   */
  public PersistentAccountRepository(ConnectionProvider connectionProvider, TransactionValidator validator) {
    this.connectionProvider = connectionProvider;
    this.validator = validator;
  }


  /**
   * Validates and deposits funds
   *
   * @param amount funds to deposit
   * @throws ValidationException
   */
  @Override
  public Double deposit(Amount amount) throws ValidationException {
    String validationMessage = validator.validateAmount(amount.value);
    try {
      if ("".equals(validationMessage)) {
        Double depositAmount = Double.parseDouble(amount.value);
        Connection connection = connectionProvider.get();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET balance=balance+? WHERE username=?");
        preparedStatement.setDouble(1, depositAmount);
        preparedStatement.setString(2, amount.username);
        preparedStatement.executeUpdate();
        return getCurrentBalance(amount.username);
      } else {
        throw new ValidationException(validationMessage);
      }
    } catch (SQLException e) {
      throw new UserException("no such user");
    }
  }

  @Override
  public Double withdraw(Amount amount) throws ValidationException {
    String validationMessage = validator.validateAmount(amount.value);
    try {
      if ("".equals(validationMessage)) {
        Double currentBalance = getCurrentBalance(amount.username);
        Double withdrawAmount = Double.parseDouble(amount.value);
        if (currentBalance < withdrawAmount) {
          throw new ValidationException("Insufficient balance");
        }
        Connection connection = connectionProvider.get();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET balance=balance-? WHERE username=?");
        preparedStatement.setDouble(1, withdrawAmount);
        preparedStatement.setString(2, amount.username);
        preparedStatement.executeUpdate();
        return getCurrentBalance(amount.username);
      } else {
        throw new ValidationException(validationMessage);
      }
    } catch (SQLException e) {
      throw new UserException("Sorry, can't find user with this name");
    }
  }


  /**
   * Gets the current value of funds in the balance
   *
   * @param userId user identification
   * @return
   */
  @Override
  public Double getCurrentBalance(String userId) throws ValidationException {
    try {
      Connection connection = connectionProvider.get();
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM account WHERE username=?");
      preparedStatement.setString(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      Double balance = resultSet.getDouble("balance");
      return balance;
    } catch (SQLException e) {
      throw new UserException("Sorry, can't find user with this name");
    }
  }

  /**
   * initiates empty account for the user
   *
   * @param userId the users unique name
   */
  @Override
  public void createAccount(String userId) {
    try {
      Connection connection = connectionProvider.get();
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account(username, balance) values(?, ?);");
      preparedStatement.setString(1, userId);
      preparedStatement.setDouble(2, 0d);
      preparedStatement.execute();
    } catch (SQLException e) {
      throw new UserException("Sorry, can't find user with this name");
    }
  }
}
