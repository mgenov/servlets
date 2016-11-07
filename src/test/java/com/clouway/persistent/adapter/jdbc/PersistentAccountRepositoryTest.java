package com.clouway.persistent.adapter.jdbc;

import com.clouway.persistent.DatastoreCleaner;
import com.clouway.core.Account;
import com.clouway.persistent.datastore.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class PersistentAccountRepositoryTest {
  private PersistentAccountRepository repo;

  @Before
  public void setUp() throws Exception {
    repo = new PersistentAccountRepository(new DataStore(new ConnectionProvider()));
    DatastoreCleaner datastoreCleaner = new DatastoreCleaner("accounts");
    datastoreCleaner.perform();
  }

  @Test
  public void happyPath() throws Exception {
    Account account = new Account("John", "123", 123);

    repo.register(account);
    Account actual = repo.getByName("John").get();

    assertThat(actual, is(account));
  }

  @Test
  public void getUnknown() throws Exception {
    Optional<Account > actual = repo.getByName("test");

    assertFalse(actual.isPresent());
  }
}