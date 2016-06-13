package com.clouway.bank;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserRepository {

  void register(User user);

  User findByName(String userName);
}
