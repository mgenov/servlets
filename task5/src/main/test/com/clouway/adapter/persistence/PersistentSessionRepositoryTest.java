package com.clouway.adapter.persistence;

import com.clouway.core.ConnectionProvider;
import com.clouway.core.Session;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public class PersistentSessionRepositoryTest {
  private ConnectionProvider connectionProvider = new FakeJdbcConnectionProvider();
  private PersistentSessionRepository sessionRepository = new PersistentSessionRepository(connectionProvider);

  @Before
  public void cleanUp() {
    sessionRepository.deleteAll();
  }

  @Test
  public void create() throws Exception {
    Session session = new Session("admin@abv.bg", "24-15-2135-135-316-1515");
    sessionRepository.create(session);

    Session expected = sessionRepository.get("24-15-2135-135-316-1515");

    assertThat(expected, is(session));
  }

  @Test
  public void deleteById() throws Exception {
    String sessionID = "24-15-2135-135-316-1515";
    sessionRepository.delete(sessionID);
    Session actual = sessionRepository.get("24-15-2135-135-316-1515");
    assertThat(null, is(actual));
  }
}
