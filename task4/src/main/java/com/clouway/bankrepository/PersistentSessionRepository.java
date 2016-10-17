package com.clouway.bankrepository;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.clouway.persistent.datastore.DataStore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class PersistentSessionRepository implements SessionRepository {
    private DataStore dataStore;

    public PersistentSessionRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void save(Session session) {
        String query = "insert into session(id,name,expires) values(?,?,?);";
        try {
            dataStore.update(query, session.id, session.username, session.timestamp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Session> getByName(String username) {
        String query = "select * from session where name=?;";
        ResultSet set;
        Optional result = Optional.empty();
        try {
            set = dataStore.execute(query, username);
            if (set.next()) {
                result = Optional.of(new Session(set.getString(1), set.getString(2),set.getTimestamp(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(String name, Session newSession) {
        String query = "update session set id=?,expires=? where name=?;";
        try {
            dataStore.update(query, newSession.id, newSession.timestamp, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String username) {
        String query = "delete from session where name=?;";
        try {
            dataStore.update(query, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
