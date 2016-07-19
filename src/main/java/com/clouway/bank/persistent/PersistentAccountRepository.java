package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.UserException;
import com.clouway.bank.core.ValidationException;

/**
 * Account data storing
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentAccountRepository implements AccountRepository {
  private DataStore dataStore;

  public PersistentAccountRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }


  /**
   * Deposits funds
   *
   * @param amount funds to deposit
   * @throws UserException
   */
  @Override
  public Double deposit(Amount amount) throws ValidationException {
    String updateQuery = "UPDATE account SET balance=balance+? WHERE username=?";
    dataStore.executeQuery(updateQuery, amount.value, amount.userId);

    return getCurrentBalance(amount.userId);
  }

  @Override
  public Double withdraw(Amount amount) throws ValidationException {
    String updateQuery = "UPDATE account SET balance=balance-? WHERE username=?";
    if (getCurrentBalance(amount.userId) < amount.value) {
      throw new ValidationException("insufficient amount");
    }

    dataStore.executeQuery(updateQuery, amount.value, amount.userId);

    return getCurrentBalance(amount.userId);
  }


  /**
   * Gets the current value of funds in the balance
   *
   * @param userId user identification
   * @return
   */
  @Override
  public Double getCurrentBalance(String userId) throws ValidationException {
    String selectQuery = "SELECT balance FROM account WHERE username=?";
    RowFetcher<Double> rowFetcher = rs -> rs.getDouble("balance");

    return dataStore.fetchRows(selectQuery, rowFetcher, userId).get(0);
  }

  /**
   * initiates empty account for the user
   *
   * @param userId the users unique name
   */
  @Override
  public void createAccount(String userId) {
    String insertQuery = "INSERT INTO account(username, balance) VALUES(?, ?);";
    dataStore.executeQuery(insertQuery, userId, 0D);
  }
}
