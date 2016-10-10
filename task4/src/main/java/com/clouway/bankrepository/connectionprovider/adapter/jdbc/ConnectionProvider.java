package com.clouway.bankrepository.connectionprovider.adapter.jdbc;

import com.clouway.bankrepository.connectionprovider.core.Provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {

    public final String database;
    public final String user;
    public final String password;

    public ConnectionProvider(String database, String user, String password) {
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection get() {
        Connection connection;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/" + database, user, password);
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
