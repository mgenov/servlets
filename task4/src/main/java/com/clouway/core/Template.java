package com.clouway.core;

/**
 * This {@code Template} interface provides the methods that return a
 * String with replaced values
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface Template {

    /**
     * Puts the placeHolder and the value in a map
     *
     * @param placeHolder the key in the map, used to match the value to be replaced
     * @param value the value in the map, replaces the matched value
     */
    public void put(String placeHolder, String value);

    /**
     * Replaces the matched values
     *
     * @return the evaluated String
     */
    public String evaluate();
}
