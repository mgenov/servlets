package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.TransactionValidator;

import javax.xml.bind.ValidationException;
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
   * @param username user identification
   * @param amount   amount of funds to deposit
   * @throws ValidationException
   */
  @Override
  public Double deposit(String username, String amount) throws ValidationException {
    String validationMessage = validator.validateAmount(amount);
    try {
      if ("".equals(validationMessage)) {
        Double depositAmount = Double.parseDouble(amount);
        Connection connection = connectionProvider.get();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET balance=balance+? WHERE username=?");
        preparedStatement.setDouble(1, depositAmount);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        return getCurrentBalance(username);
      } else {
        throw new ValidationException(validationMessage);
      }
    } catch (SQLException e) {
      throw new ValidationException("no such user");
    }
  }

  /**
   * Gets the current amount of funds in the balance
   *
   * @param username user identification
   * @return
   */
  @Override
  public Double getCurrentBalance(String username) throws ValidationException {
    try {
      Connection connection = connectionProvider.get();
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM account WHERE username=?");
      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      Double balance = resultSet.getDouble("balance");
      return balance;
    } catch (SQLException e) {
      throw new ValidationException("no such user!");
    }
  }
}
