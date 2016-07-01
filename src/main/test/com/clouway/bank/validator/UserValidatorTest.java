package com.clouway.bank.validator;

import com.clouway.bank.core.User;
import com.clouway.bank.core.Validator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class UserValidatorTest {
  private Validator<User> validator = new UserValidator();

  @Test
  public void userNameIsValid() throws Exception {
    User user = new User("Ivan", "mail@abv.bg", "pass123");
    String result = validator.validate(user);

    assertThat(result, is(""));
  }

  @Test
  public void userNameContainsDigits() throws Exception {
    User user = new User("Ivan66", "ivan@abv.bg", "pass123");
    String result = validator.validate(user);

    assertThat(result, is("The name should be between 1 and 15 letters"));
  }

  @Test
  public void userNameIsToLong() throws Exception {
    User user = new User("Ivannnnnnnnnnnnnnnnnnnnnn", "ivan@abv.bg", "pass123");
    String result = validator.validate(user);

    assertThat(result, is("The name should be between 1 and 15 letters"));
  }

  @Test
  public void emailIsNotInValidFormat() throws Exception {
    User user = new User("Ivan", "ivan.abv.bg", "pass123");
    String result = validator.validate(user);

    assertThat(result, is("The email should be in valid format. The valid format is some@abv.bg"));
  }

  @Test
  public void passwordIsToShort() throws Exception {
    User user = new User("Ivan", "ivan@abv.bg", "pass");
    String result = validator.validate(user);

    assertThat(result, is("The password should be at least 6 symbols"));
  }

  @Test
  public void passwordIsToLong() throws Exception {
    User user = new User("Ivan", "ivan@abv.bg", "pass123456789012345678");
    String result = validator.validate(user);

    assertThat(result, is("The password should be at least 6 symbols"));
  }
}
