package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to generateId random String sequence
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface IdGenerator {
  /**
   * Will generateId sequence of characters
   *
   * @return generated id
   */
  String generateId();
}
