package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentSessionRepository;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentSessionRepositoryTest {
  private Provider<Connection> provider;

  @Before
  public void setUp() throws Exception {
    provider = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");
  }

  @Test
  public void save() throws Exception {
    SessionRepository repository = new PersistentSessionRepository(provider);
    Session session = new Session("sessionId", "user@domain.com", getTime("12:00:12"));

    repository.save(session);
    Session actual = repository.findSession("user@domain.com");

    assertThat(actual, is(equalTo(session)));
  }

  private long getTime(String time) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ssss");

    return dateFormat.parse(time).getTime();
  }
}
