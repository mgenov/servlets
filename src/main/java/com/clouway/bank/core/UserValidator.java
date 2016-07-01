package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserValidator {

  /**
   * Validates the users fields
   *
   * @param user user object
   * @return validation message
   */
  String validate(User user);

  /**
   * Validates if the passwords match
   *
   * @param password        the users password
   * @param confirmPassword the users confirmation password
   * @return validation message
   */
  String passwordsMatch(String password, String confirmPassword);
}
