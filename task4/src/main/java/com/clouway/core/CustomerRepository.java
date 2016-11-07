package com.clouway.core;

import java.util.Optional;

/**
 * This {@code CustomerRepository} interface provides the methods
 * to be implemented for work with the Customer table in the Bank database
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface CustomerRepository {

    /**
     * Adds a new record to the customer database
     *
     * @param customer which values would be registered in the DB
     */
    void register(Customer customer);

    /**
     * Updates an existing record with the new balance
     *
     * @param name    used to match the row which is going to be updated
     * @param balance used to replace the old value of balance
     */
    void updateBalance(String name, Integer balance);

    /**
     * Returns object of class Customer if such exist corresponding to the username search parameter
     *
     * @param username used to match the row which is going to be returned
     * @return Customer object
     */
    Optional<Customer> getByName(String username);
}
