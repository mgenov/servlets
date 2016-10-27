package com.clouway.core;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface Template {

  void put(String placeHolder, String value);

  String evaluate();
}