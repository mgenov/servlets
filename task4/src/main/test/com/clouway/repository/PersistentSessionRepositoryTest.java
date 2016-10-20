package com.clouway.repository;

import com.clouway.bankrepository.PersistentSessionRepository;
import com.clouway.core.Session;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.datastore.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentSessionRepositoryTest {
    private ConnectionProvider provider = new ConnectionProvider("bank", "postgres", "123");
    private DataStore dataStore = new DataStore(provider);
    private PersistentSessionRepository sessionRepository = new PersistentSessionRepository(dataStore);
    private Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

    @Before
    public void setUp() throws Exception {
        try {
            Connection connection = provider.get();
            Statement statement = connection.createStatement();
            statement.executeUpdate("TRUNCATE TABLE session;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void happyPath() {
        Session expected = new Session("encryption", "username", timestamp);

        sessionRepository.save(expected);
        Session actual = sessionRepository.getByName("username").get();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void update() {
        Timestamp newTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        Session oldSession = new Session("encryption", "username", timestamp);
        Session updated = new Session("newEncryption", "username", newTimestamp);

        sessionRepository.save(oldSession);
        sessionRepository.update("username" ,updated);
        Session actual = sessionRepository.getByName("username").get();

        assertTrue(updated.equals(actual));
    }

    @Test
    public void delete() {
        Session expected = new Session("encryption", "username", timestamp);

        sessionRepository.save(expected);
        sessionRepository.delete("username");

        Optional actual = sessionRepository.getByName("username");

        assertFalse(expected.equals(actual));
        assertFalse(actual.isPresent());
    }
}
