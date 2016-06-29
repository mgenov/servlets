package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class TransactionException extends RuntimeException {
  public TransactionException(String message) {
    super(message);
  }
}
