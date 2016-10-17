package com.clouway.core;

import java.sql.Timestamp;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class Session {
    public final String id;
    public final String username;
    public final Timestamp timestamp;

    public Session(String id, String username, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (id != null ? !id.equals(session.id) : session.id != null) return false;
        if (username != null ? !username.equals(session.username) : session.username != null) return false;
        return timestamp != null ? timestamp.equals(session.timestamp) : session.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
