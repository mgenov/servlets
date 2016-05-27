package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 25.05.16.
 */
public interface UserRepository {

  void register(User user);

  User findByEmail(String email);

  boolean authorize(String email, String password);
}
