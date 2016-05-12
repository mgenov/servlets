package com.clouway.bank.http.validation;

import com.clouway.bank.core.User;
import com.clouway.bank.core.UserValidator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserDataValidatorTest {
  private UserValidator userValidator;

  @Before
  public void setUp() {
    userValidator = new UserDataValidator();
  }

  @Test
  public void validUser() {
    User peter = new User("Peter", "123456");

    String validationMessage = userValidator.validate(peter);

    assertThat(validationMessage, is(equalTo("")));
  }

  @Test
  public void shortUsername() {
    User jack = new User("Jack", "123456");

    String validationMessage = userValidator.validate(jack);

    assertThat(validationMessage, is(equalTo("The userId must be between 5 and 15 characters (alphabetic and numeric).")));
  }

  @Test
  public void longName() {
    User jack = new User("TheUsernameWithoutAName", "123456");

    String validationMessage = userValidator.validate(jack);

    assertThat(validationMessage, is(equalTo("The userId must be between 5 and 15 characters (alphabetic and numeric).")));
  }

  @Test
  public void unacceptableCharactersInUsername() {
    User jack = new User("'' or 1=1", "123456");

    String validationMessage = userValidator.validate(jack);

    assertThat(validationMessage, is(equalTo("The userId must be between 5 and 15 characters (alphabetic and numeric).")));
  }

  @Test
  public void shortPassword() {
    User johny = new User("Johny", "12345");

    String validationMessage = userValidator.validate(johny);

    assertThat(validationMessage, is(equalTo("Password must be between 6 and 20 characters (alphabetic and numeric).")));
  }

  @Test
  public void longPassword() {
    User johny = new User("Johny", "012345678901234567890");

    String validationMessage = userValidator.validate(johny);

    assertThat(validationMessage, is(equalTo("Password must be between 6 and 20 characters (alphabetic and numeric).")));
  }

  @Test
  public void unacceptableCharactersInPassword() {
    User ricky = new User("Ricky", "'' or 1=1");

    String validationMessage = userValidator.validate(ricky);

    assertThat(validationMessage, is(equalTo("Password must be between 6 and 20 characters (alphabetic and numeric).")));
  }

  @Test
  public void passwordsMatch() {
    User kristyan = new User("Kristyan", "123456");

    String validationMessage = userValidator.passwordsMatch(kristyan.password, "123456");
    assertThat(validationMessage, is(equalTo("")));
  }

  @Test
  public void passwordsDoNotMatch() {
    User kristyan = new User("Kristyan", "123456");

    String validationMessage = userValidator.passwordsMatch(kristyan.password, "qwerty");
    assertThat(validationMessage, is(equalTo("Passwords don't match")));
  }
}
