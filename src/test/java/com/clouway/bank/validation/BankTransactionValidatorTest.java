package com.clouway.bank.validation;


import com.clouway.bank.core.TransactionValidator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class BankTransactionValidatorTest {
  private TransactionValidator transactionValidator;

  @Before
  public void setUp() {
    transactionValidator = new BankTransactionValidator();
  }

  @Test
  public void validAmount() {
    String validationMessage = transactionValidator.validateAmount("12.50");
    assertThat(validationMessage, is(equalTo("")));
  }

  @Test
  public void noAmount() {
    String validationMessage = transactionValidator.validateAmount("e");
    assertThat(validationMessage, is(equalTo("incorrect amount, has to have a positive number '.' and at least one digit afterwards")));
  }

  @Test
  public void notADouble() {
    String validationMessage = transactionValidator.validateAmount("5");
    assertThat(validationMessage, is(equalTo("incorrect amount, has to have a positive number '.' and at least one digit afterwards")));
  }
}