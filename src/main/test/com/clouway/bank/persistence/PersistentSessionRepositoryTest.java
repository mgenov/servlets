package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentSessionRepository;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentSessionRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private Provider<Connection> provider;
  private PreparedStatement statement;

  @Before
  public void setUp() throws Exception {
    provider = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");

    statement = provider.get().prepareStatement("truncate table sessions;");
    statement.executeUpdate();
  }

  @After
  public void tearDown() throws Exception {
    statement.close();
  }

  @Test
  public void save() throws Exception {
    final SessionRepository repository = new PersistentSessionRepository(provider);
    final Session session = new Session("sessionId", "user@domain.com", getTime("12:12:1002"));

    repository.save(session);
    Session actual = repository.findSessionById("sessionId");

    assertThat(actual, is(equalTo(session)));
  }

  private long getTime(String timeAsString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ssss");

    Date date = null;
    try {
      date = simpleDateFormat.parse(timeAsString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
