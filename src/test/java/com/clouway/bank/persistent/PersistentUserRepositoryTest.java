package com.clouway.bank.persistent;

import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.User;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepositoryTest {
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  DataStore dataStore;

  private PersistentUserRepository userRepository;
  private UserRepositoryUtility userRepositoryUtility;

  @Before
  public void setUp() {
    userRepository = new PersistentUserRepository(dataStore);
    userRepositoryUtility = new UserRepositoryUtility(connectionRule.getConnection());
  }

  @After
  public void tearDown() {
    userRepositoryUtility.clearUsersTable();
  }

  @Test
  public void registerUser() {
    User john = new User("John01", "123456");

    List<User> johnFromStore = new ArrayList<>();
    johnFromStore.add(john);

    context.checking(new Expectations() {{
      oneOf(dataStore).fetchRows(with(equalTo("SELECT * FROM users WHERE username = ?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"John01"})));
      will(returnValue(new ArrayList<User>()));

      oneOf(dataStore).executeQuery("INSERT INTO users(username, password) VALUES(?, ?);", new Object[]{"John01", "123456"});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT * FROM users WHERE username = ?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"John01"})));
      will(returnValue(johnFromStore));
    }});
    userRepository.register(john);

    User returnedUser = userRepository.getUserById("John01");

    assertThat(returnedUser, is(equalTo(john)));
  }

}
