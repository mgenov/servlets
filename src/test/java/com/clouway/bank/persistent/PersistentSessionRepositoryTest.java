package com.clouway.bank.persistent;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.FakeConnectionProvider;
import com.clouway.bank.utils.SessionRepositoryUtility;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentSessionRepositoryTest {
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  private SessionRepository sessionRepository;

  @Before
  public void setUp() throws SQLException {
    sessionRepository = new PersistentSessionRepository(new FakeConnectionProvider());
    Connection connection = connectionRule.getConnection();
    new SessionRepositoryUtility(connection).clearSessionTable();
    connection.close();
  }

  @Test
  public void createSession() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2016, 7, 6);
    calendar.set(Calendar.MILLISECOND, 0);
    Long time = calendar.getTimeInMillis();

    Session session = new Session("1", "Ivan01", time);

    sessionRepository.create(session);

    Session returnedSession = sessionRepository.retrieve("1");
    assertThat(returnedSession, is(equalTo(session)));
    assertThat(returnedSession.expirationTime.toString(), is(equalTo(session.expirationTime.toString())));
  }

  @Test
  public void updateExistingSession() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Session session = new Session("2", "Petar3", calendar.getTimeInMillis());

    sessionRepository.create(session);

    calendar.set(Calendar.YEAR, 2017);
    session = new Session("2", "Petar3", calendar.getTimeInMillis());
    sessionRepository.update(session);

    Session returnedSession = sessionRepository.retrieve("2");
    assertThat(returnedSession.expirationTime.toString(), is(equalTo(session.expirationTime.toString())));
  }

  @Test
  public void countActiveSession() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Session session = new Session("2", "Petar3", calendar.getTimeInMillis());

    sessionRepository.create(session);


    calendar.set(Calendar.YEAR, 2013);
    int activeSessions = sessionRepository.countActive(calendar.getTimeInMillis());

    assertThat(activeSessions, is(equalTo(1)));
  }

  @Test
  public void countTwoActiveSessions() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Session session = new Session("2", "Petar3", calendar.getTimeInMillis());
    calendar.set(Calendar.DAY_OF_MONTH, 10);
    Session secondSession = new Session("5", "Ivaylo", calendar.getTimeInMillis());

    sessionRepository.create(session);
    sessionRepository.create(secondSession);


    calendar.set(Calendar.YEAR, 2014);
    int activeSessions = sessionRepository.countActive(calendar.getTimeInMillis());

    assertThat(activeSessions, is(equalTo(2)));
  }

  @Test
  public void countOneActiveSessionsWhenOneInactive() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Session session = new Session("2", "Petar3", calendar.getTimeInMillis());
    calendar.set(Calendar.DAY_OF_MONTH, 7);
    Session secondSession = new Session("5", "Ivaylo", calendar.getTimeInMillis());

    sessionRepository.create(session);
    sessionRepository.create(secondSession);


    int activeSessions = sessionRepository.countActive(calendar.getTimeInMillis());

    assertThat(activeSessions, is(equalTo(1)));
  }
}