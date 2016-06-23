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
   * @param username user identification
   * @param amount   amount of funds to deposit
   * @throws ValidationException
   */
  Double deposit(String username, String amount) throws ValidationException;


  /**
   * will return the current state of the balance
   *
   * @param username user identification
   * @return the balance for the given user
   */
  Double getCurrentBalance(String username) throws ValidationException;
}
