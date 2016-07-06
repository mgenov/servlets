package com.clouway.bank.persistent;

import com.clouway.bank.core.User;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.FakeConnectionProvider;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

  private PersistentUserRepository userRepository;
  private UserRepositoryUtility userRepositoryUtility;

  @Before
  public void setUp() {
    userRepository = new PersistentUserRepository(new FakeConnectionProvider());
    userRepositoryUtility = new UserRepositoryUtility(connectionRule.getConnection());
  }

  @After
  public void tearDown() {
    userRepositoryUtility.clearUsersTable();
  }

  @Test
  public void registerUser() {
    User john = new User("John", "123456");


    userRepository.register(john);

    User returnedUser = userRepository.getUserById("John");

    assertThat(returnedUser, is(equalTo(john)));
  }

}
