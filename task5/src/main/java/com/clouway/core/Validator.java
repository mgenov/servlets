package com.clouway.core;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 27.05.16.
 */
public interface Validator {
  boolean isValid(String email, String password);

  boolean isValid(String username, String password, String email);

  boolean isValid(String amount);
}
