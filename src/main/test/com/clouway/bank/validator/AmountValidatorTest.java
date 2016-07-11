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
  private Validator validator;

  @Before
  public void setUp() throws Exception {
    validator = new AmountValidator();
  }

  @Test
  public void validateValidCash() throws Exception {
    String cash = "123.00";

    String actual = validator.validate(cash);

    assertThat(actual, is(""));
  }

  @Test
  public void validateWholeCash() throws Exception {
    String cash = "100";

    String actual = validator.validate(cash);

    assertThat(actual, is(""));
  }

  @Test
  public void validateToLongWholePiece() throws Exception {
    String cash = "123456.00";

    String actual = validator.validate(cash);

    assertThat(actual, is("The format of the amount is not valid. Valid format is 12.00. Please enter valid data!"));
  }

  @Test
  public void validateToLongFractionalPiece() throws Exception {
    String cash = "100.1234";

    String actual = validator.validate(cash);

    assertThat(actual, is("The format of the amount is not valid. Valid format is 12.00. Please enter valid data!"));
  }

  @Test
  public void validateWrongFormat() throws Exception {
    String cash = "asas";

    String actual = validator.validate(cash);

    assertThat(actual, is("The format of the amount is not valid. Valid format is 12.00. Please enter valid data!"));
  }
}
