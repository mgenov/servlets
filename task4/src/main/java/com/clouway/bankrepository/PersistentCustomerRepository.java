package com.clouway.bankrepository;

import com.clouway.core.Customer;
import com.clouway.core.CustomerRepository;
import com.clouway.core.Provider;
import com.clouway.persistent.datastore.DataStore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentCustomerRepository implements CustomerRepository {
    private final DataStore dataStore;

    public PersistentCustomerRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void register(Customer customer) {
        String query = "insert into customer(name,password,balance) values(?,?,?);";
        try {
            dataStore.update(query, customer.name, customer.password, customer.balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBalance(String name, Integer balance) {
        String query = "update customer set balance=? where name=?;";
        try {
            dataStore.update(query, balance, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> getByName(String username) {
        String query = "select * from customer where name=?;";
        ResultSet set;
        Optional result = Optional.empty();
        try {
            set = dataStore.execute(query, username);
            if (set.next()) {
                result = Optional.of(new Customer(set.getInt(1), set.getString(2), set.getString(3), set.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
