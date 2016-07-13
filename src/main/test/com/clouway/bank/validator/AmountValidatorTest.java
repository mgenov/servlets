package com.clouway.bank.validator;

import com.clouway.bank.core.Validator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AmountValidatorTest {
  private Validator<String> validator;

  @Before
  public void setUp() throws Exception {
    validator = new AmountValidator();
  }

  @Test
  public void validAmount() throws Exception {
    String amount = "123.00";

    String actual = validator.validate(amount);
    String expectedErrorMessage = "";

    assertThat(actual, is(expectedErrorMessage));
  }

  @Test
  public void tooLongWholePart() throws Exception {
    String amount = "123456.00";

    String actual = validator.validate(amount);
    String expectedErrorMessage = "Amount must be positive number.";

    assertThat(actual, is(expectedErrorMessage));
  }

  @Test
  public void tooLongFractionPart() throws Exception {
    String amount = "100.1234";

    String actual = validator.validate(amount);
    String expectedErrorMessage = "Amount must be positive number.";

    assertThat(actual, is(expectedErrorMessage));
  }

  @Test
  public void amountIsLetters() throws Exception {
    String amount = "asas";

    String actual = validator.validate(amount);
    String expectedErrorMessage = "Amount must be positive number.";

    assertThat(actual, is(expectedErrorMessage));
  }

  @Test
  public void amountIsNegative() throws Exception {
    String amount = "-10.20";

    String actual = validator.validate(amount);
    String expectedErrorMessage = "Amount must be positive number.";

    assertThat(actual, is(expectedErrorMessage));
  }
}
