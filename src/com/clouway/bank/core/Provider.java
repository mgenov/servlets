package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to provide resource
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Provider<T> {
  /**
   * Will provide resource
   *
   * @return resource
   */
  T get();
}
