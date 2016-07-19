package com.clouway.bank.persistent;

import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.SessionRepositoryUtility;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentSessionRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  @Mock
  DataStore dataStore;
  private SessionRepository sessionRepository;

  @Before
  public void setUp() throws SQLException {
    sessionRepository = new PersistentSessionRepository(dataStore);
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

    List<Session> sessions = new ArrayList<>();
    sessions.add(session);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"1", "Ivan01", new Timestamp(time)});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT sessionid, username, expirationtime FROM login WHERE sessionid=?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"1"})));
      will(returnValue(sessions));
    }});


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

    Long time = calendar.getTimeInMillis();
    calendar.set(Calendar.YEAR, 2017);
    Long updateTime = calendar.getTimeInMillis();

    Session session = new Session("2", "Petar3", time);
    Session updateSession = new Session("2", "Petar3", updateTime);

    List<Session> sessions = new ArrayList<>();
    sessions.add(session);

    List<Session> updatedSessions = new ArrayList<>();
    updatedSessions.add(updateSession);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"2", "Petar3", new Timestamp(time)});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT sessionid, username, expirationtime FROM login WHERE sessionid=?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"2"})));
      will(returnValue(sessions));

      oneOf(dataStore).executeQuery("UPDATE login SET expirationtime=? WHERE sessionid=?;", new Object[]{new Timestamp(updateTime), "2"});
    }});

    sessionRepository.create(session);

    sessionRepository.update(updateSession);

    Session returnedSession = sessionRepository.retrieve("2");
    assertThat(returnedSession.expirationTime.toString(), is(equalTo(session.expirationTime.toString())));
  }

  @Test
  public void countActiveSession() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Long time = calendar.getTimeInMillis();
    Session session = new Session("2", "Petar3", time);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"2", "Petar3", new Timestamp(time)});
    }});

    sessionRepository.create(session);

    calendar.set(Calendar.YEAR, 2013);
    Long updatedTime = calendar.getTimeInMillis();
    context.checking(new Expectations() {{
      oneOf(dataStore).fetchRows(with(equalTo("SELECT count(*) FROM login WHERE expirationtime>?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{new Timestamp(updatedTime)})));
      will(returnValue(Arrays.asList(new Integer[]{1})));
    }});
    int activeSessions = sessionRepository.countActive(calendar.getTimeInMillis());

    assertThat(activeSessions, is(equalTo(1)));
  }

  @Test
  public void countTwoActiveSessions() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Long originalTime = calendar.getTimeInMillis();
    Session session = new Session("2", "Petar3", originalTime);
    calendar.set(Calendar.DAY_OF_MONTH, 20);
    Session secondSession = new Session("5", "Ivaylo", calendar.getTimeInMillis());

    List<Integer> activeSessionsCount = new ArrayList<>();
    activeSessionsCount.add(2);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"2", "Petar3", new Timestamp(originalTime)});

      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"5", "Ivaylo", new Timestamp(calendar.getTimeInMillis())});


    }});

    sessionRepository.create(session);
    sessionRepository.create(secondSession);

    calendar.set(Calendar.YEAR, 2014);
    Long queryTime = calendar.getTimeInMillis();

    context.checking(new Expectations() {{
      oneOf(dataStore).fetchRows(with(equalTo("SELECT count(*) FROM login WHERE expirationtime>?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{new Timestamp(queryTime)})));
      will(returnValue(activeSessionsCount));
    }});
    int activeSessions = sessionRepository.countActive(queryTime);

    assertThat(activeSessions, is(equalTo(2)));
  }

  @Test
  public void countOneActiveSessionsWhenOneInactive() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2015, 2, 15);
    calendar.set(Calendar.MILLISECOND, 0);

    Long originalTime = calendar.getTimeInMillis();
    Session session = new Session("2", "Petar3", originalTime);
    calendar.set(Calendar.DAY_OF_MONTH, 7);
    Session secondSession = new Session("5", "Ivaylo", calendar.getTimeInMillis());

    List<Integer> activeSessionsCount = new ArrayList<>();
    activeSessionsCount.add(1);

    Long queryTime = calendar.getTimeInMillis();

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"2", "Petar3", new Timestamp(originalTime)});

      oneOf(dataStore).executeQuery("INSERT INTO login(sessionid, username, expirationtime) VALUES(?, ?, ?)", new Object[]{"5", "Ivaylo", new Timestamp(calendar.getTimeInMillis())});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT count(*) FROM login WHERE expirationtime>?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{new Timestamp(queryTime)})));
      will(returnValue(activeSessionsCount));
    }});

    sessionRepository.create(session);
    sessionRepository.create(secondSession);


    int activeSessions = sessionRepository.countActive(queryTime);

    assertThat(activeSessions, is(equalTo(1)));
  }
}