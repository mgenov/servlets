package com.clouway.bank.http.validation;

import com.clouway.bank.core.User;
import com.clouway.bank.core.UserValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserDataValidator implements UserValidator {

  /**
   * Validates the users fields
   *
   * @param user user object
   * @return validation message ("" if the fields are valid)
   */
  @Override
  public String validate(User user) {
    String message = validateUsername(user.id);
    message += validatePassword(user.password);
    return message;
  }

  /**
   * validates the validateUsername
   *
   * @param username the user name to be validated
   * @return validations message ("" if the validateUsername matches the criteria)
   */
  private String validateUsername(String username) {
    String nameRegex = "^([A-Za-z0-9]){5,15}$";
    Pattern pattern = Pattern.compile(nameRegex);
    Matcher matcher = pattern.matcher(username);
    if (matcher.matches()) {
      return "";
    } else {
      return "The userId must be between 5 and 15 characters (alphabetic and numeric).";
    }
  }


  /**
   * validates the validatePassword
   *
   * @param password the user's validatePassword to be validated
   * @return validations message ("" if the validatePassword matches the criteria)
   */
  private String validatePassword(String password) {
    String passRegex = "^([A-Za-z0-9]){6,20}$";
    Pattern pattern = Pattern.compile(passRegex);
    Matcher matcher = pattern.matcher(password);
    if (matcher.matches()) {
      return "";
    } else {
      return "Password must be between 6 and 20 characters (alphabetic and numeric).";
    }
  }

  /**
   * Validates if the passwords match
   *
   * @param password        the users validatePassword
   * @param confirmPassword the users confirmation validatePassword
   * @return validation message ("" if the passwords match)
   */
  @Override
  public String passwordsMatch(String password, String confirmPassword) {
    if (password.equals(confirmPassword)) {
      return "";
    } else {
      return "Passwords don't match";
    }
  }
}
