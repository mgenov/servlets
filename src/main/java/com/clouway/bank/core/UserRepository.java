package com.clouway.bank.core;

import java.util.Map;

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

  /**
   * Will receive all users by email and password
   *
   * @param email user email
   * @return map with users and emails
   */
  Map<String, String> findAll(String email);
}
