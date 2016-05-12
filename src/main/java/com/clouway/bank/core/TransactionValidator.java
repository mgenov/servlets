package com.clouway.bank.core;

/**
 * Will validate the transaction data
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface TransactionValidator {

  /**
   * Validates the value
   *
   * @param amount to be validated
   * @return validation message
   */
  String validateAmount(String amount);
}
