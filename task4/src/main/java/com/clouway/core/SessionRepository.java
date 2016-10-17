package com.clouway.core;

import java.util.Optional;

/**
 * This {@code Session} interface provides the methods
 * to be implemented for work with the Session table in the Bank database
 *
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface SessionRepository {

    /**
     * Adds a new record to the session table
     *
     * @param session session object to be added
     */
    void save(Session session);

    /**
     * Returns object of class Session if such exist corresponding to the username search parameter
     *
     * @param username used to match the row which is going to be returned
     * @return Session object
     */
    Optional<Session> getByName(String username);

    /**
     * Updates record in the Session table if such exist corresponding to the username search parameter
     *
     * @param updated used to match the record to be updated
     */
    void update(String name, Session updated);

    /**
     * Deletes record in the Session table if such exist corresponding to the username search parameter
     *
     * @param username used to match the record to be deleted
     */
    void delete(String username);
}
