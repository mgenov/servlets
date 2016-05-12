package com.clouway.bank.persistent;

import com.clouway.bank.core.Session;
import com.clouway.bank.core.SessionRepository;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.FakeConnectionProvider;
import com.clouway.bank.utils.LoginRepositoryUtility;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
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
  public void setUp() {
    sessionRepository = new PersistentSessionRepository(new FakeConnectionProvider());
    Connection connection = connectionRule.getConnection();
    new LoginRepositoryUtility(connection).clearLoginTable();
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
}