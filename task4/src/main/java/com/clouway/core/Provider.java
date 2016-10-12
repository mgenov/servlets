package com.clouway.core;

/**
 * This {@code Provider} interface provides a method that returns a value
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface Provider<T> {

    /**
     * @return value of specified type
     */
    T get();
}
