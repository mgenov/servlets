package com.clouway.bank;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationValidatorTest {
  private RegistrationValidator validator;

  @Before
  public void setUp() {
    validator = new RegistrationValidator();
  }

  @Test
  public void validUsernameField() {

    String validationMessage = validator.validateField("Username", "Krasimir", "[a-zA-Z]");

    assertThat("", is(equalTo(validationMessage)));
  }

  @Test
  public void inValidUsernameField() {

    String validationMessage = validator.validateField("Username", "Krasimir2", "^[a-zA-Z]$");

    assertThat(validationMessage, is(equalTo("Username is invalid")));
  }
}
