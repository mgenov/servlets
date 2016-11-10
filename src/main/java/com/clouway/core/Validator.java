package com.clouway.core;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface Validator<T> {
  boolean check(T value);
}
