package com.clouway.bankrepository;

import com.clouway.core.Customer;
import com.clouway.persistent.adapter.jdbc.ConnectionProvider;
import com.clouway.persistent.datastore.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentCustomerRepositoryTest {
    private ConnectionProvider provider = new ConnectionProvider("bank", "postgres", "123");
    private DataStore dataStore = new DataStore(provider);
    private PersistentCustomerRepository customerRepository = new PersistentCustomerRepository(dataStore);

    @Before
    public void setUp() throws Exception {
        try {
            Connection connection = provider.get();
            Statement statement = connection.createStatement();
            statement.executeUpdate("ALTER SEQUENCE customer_id RESTART WITH 1;");
            statement.executeUpdate("TRUNCATE TABLE customer;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void happyPath() {
        Customer customer = new Customer(1, "Borislav", "mypass", 0);

        customerRepository.register(customer);
        Customer actual = customerRepository.getByName("Borislav").get();

        assertTrue(customer.equals(actual));
    }

    @Test
    public void getUnknown() {
        Optional<Customer> customer = customerRepository.getByName("Borislav");

        assertTrue(!customer.isPresent());
    }
}
