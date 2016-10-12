package main.java.com.clouway.core;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface Provider<T> {

  /**
   * @return value of specified type
   */
  T get();
}