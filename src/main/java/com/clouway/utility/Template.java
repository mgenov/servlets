package com.clouway.utility;

/**
 * Gets string and replaces the given place holders
 * with associated value, and gives the new string back
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface Template {

  /**
   * setting the original string template value
   *
   * @param template the original string value
   */
  void setTemplate(String template);

  /**
   * associates place holders with values to be substituted
   *
   * @param placeHolder place holder to be substituted
   * @param value       the value to be set
   */
  void put(String placeHolder, String value);

  /**
   * substitutes the placeholders for the values
   * and returns the new string
   *
   * @return the new string
   */
  String evaluate();
}
