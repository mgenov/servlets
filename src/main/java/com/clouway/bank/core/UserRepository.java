package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserRepository {

  /**
   * Registers user in the repository
   *
   * @param user the user object
   */
  void register(User user);

  /**
   * finds the user by name
   *
   * @param userId the user name
   * @return the User
   */
  User getUserById(String userId);
}
