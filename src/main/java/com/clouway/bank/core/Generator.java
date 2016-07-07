package com.clouway.bank.core;

/**
 * The implementation of this interface will be used to generate random String sequence
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Generator {
  /**
   * Will generate sequence of characters
   *
   * @return generated id
   */
  String generate();
}
