package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to save and retrieve data
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface UserRepository {
  /**
   * Register user
   *
   * @param user registered user
   */
  void register(User user);

  /**
   * Find registered user
   *
   * @param email email of the user
   */
  User findByEmail(String email);
}
