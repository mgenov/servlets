package com.clouway.bank.core;

/**
 * The implementation of this interface will be used for validation
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Validator<T> {
  /**
   * Will check the user
   *
   * @param object inspected object
   * @return result message from validation
   */
  String validate(T object);

  /**
   * Will check the emails and passwords
   *
   * @param email    checked email
   * @param password checked password
   * @return report from validation
   */
  String validate(String email, String password);
}
