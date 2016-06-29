package com.clouway.utility;

/**
 * Reading the contents of a source
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface Reader {

  /**
   * reads the content of a source
   *
   * @param source the source of reading
   * @return the content
   */
  String read(String source);
}
