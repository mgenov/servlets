package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to save and retrieve data for account
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface AccountRepository {
  /**
   * Will be used for creating account
   *
   * @param account created account
   */
  void createAccount(Account account);

  /**
   * Will be find account by user email
   *
   * @param email user email
   * @return account
   */
  Account findByEmail(String email);

  /**
   * Will be added amount to user
   *
   * @param email user email
   */
  void deposit(String email, Double amount);

  /**
   * Will be remove amount from user
   *
   * @param email user email
   */

  void withdraw(String email, Double amount);
}
