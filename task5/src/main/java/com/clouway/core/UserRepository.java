package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 25.05.16.
 */
public interface UserRepository {

  void register(User user);

  User findByEmail(String email);

  boolean authenticate(String email, String password);

  void deleteAll();
}
