package com.clouway.bank.http.validation;


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
  public void invalidNumber() {
    String validationMessage = transactionValidator.validateAmount("12.523");
    assertThat(validationMessage, is(equalTo("incorrect value, has to have a positive number '.' and one or two digits afterwards")));
  }
}