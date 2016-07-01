package com.clouway.bank.persistent;

import com.clouway.bank.core.User;
import com.clouway.bank.core.UserValidator;
import com.clouway.bank.core.ValidationException;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.FakeConnectionProvider;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
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
  @Mock
  UserValidator validator;
  private PersistentUserRepository userRepository;
  private UserRepositoryUtility userRepositoryUtility;

  @Before
  public void setUp() {
    userRepository = new PersistentUserRepository(new FakeConnectionProvider(), validator);
    userRepositoryUtility = new UserRepositoryUtility(connectionRule.getConnection());
  }

  @After
  public void tearDown() {
    userRepositoryUtility.clearUsersTable();
  }

  @Test
  public void registerUser() {
    User john = new User("John", "123456");

    context.checking(new Expectations() {{
      oneOf(validator).validate(john);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("123456", "123456");
      will(returnValue(""));
    }});

    userRepository.register(john, "123456");

    User returnedUser = userRepository.getUserById("John");

    assertThat(returnedUser, is(equalTo(john)));
  }

  @Test(expected = ValidationException.class)
  public void registerInvalidUser() {
    User jack = new User("Jackie", "qwerty");

    context.checking(new Expectations() {{
      oneOf(validator).validate(jack);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("qwerty", "123456");
      will(returnValue("Passwords don't match"));
    }});

    userRepository.register(jack, "123456");
  }

  @Test(expected = ValidationException.class)
  public void userAlreadyExist() {
    User molly = new User("Molly", "zxcvbn");

    context.checking(new Expectations() {{
      oneOf(validator).validate(molly);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("zxcvbn", "zxcvbn");
      will(returnValue(""));

      oneOf(validator).validate(molly);
      will(returnValue(""));

      oneOf(validator).passwordsMatch("zxcvbn", "zxcvbn");
      will(returnValue(""));
    }});

    userRepository.register(molly, "zxcvbn");
    userRepository.register(molly, "zxcvbn");
  }
}
