package com.clouway.bank.core;

import javax.xml.bind.ValidationException;

/**
 * Will store and get the account data
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface AccountRepository {

  /**
   * deposits funds
   *
   * @param amount funds to deposit
   * @throws ValidationException
   */
  Double deposit(Amount amount) throws ValidationException;

  /**
   * withdraw funds
   *
   * @param amount funds to withdraw
   * @throws ValidationException
   */
  Double withdraw(Amount amount) throws ValidationException;

  /**
   * will return the current state of the balance
   *
   * @param username user identification
   * @return the balance for the given user
   */
  Double getCurrentBalance(String username) throws ValidationException;

}
