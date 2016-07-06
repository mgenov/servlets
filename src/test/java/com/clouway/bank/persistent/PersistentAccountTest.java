package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.utils.AccountRepositoryUtility;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.xml.bind.ValidationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@RunWith(Parameterized.class)
public class PersistentAccountTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  @Mock
  ConnectionProvider connectionProvider;

  private AccountRepository accountRepository;
  private Connection connection;
  private AccountRepositoryUtility accountRepositoryUtility;
  private UserRepositoryUtility userRepositoryUtility;
  private String username = "Stanislava";
  private Double amount;

  public PersistentAccountTest(Double amount) {
    this.amount = amount;
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {10.0}, {110.0}, {243.0}, {315.0}, {46.0}, {520.0}, {63.0}
    });
  }

  @Before
  public void setUp() {
    accountRepository = new PersistentAccountRepository(connectionProvider);
    connection = connectionRule.getConnection();
    accountRepositoryUtility = new AccountRepositoryUtility(connection);
    accountRepositoryUtility.clearAccountTable();
    userRepositoryUtility = new UserRepositoryUtility(connection);
    userRepositoryUtility.clearUsersTable();
    userRepositoryUtility.registerUser("Stanislava", "123456");
  }

  @After
  public void tearDown() throws SQLException {
    accountRepositoryUtility.clearAccountTable();
    userRepositoryUtility.clearUsersTable();
    connection.close();
  }

  @Test
  public void depositFunds() throws ValidationException {
    accountRepositoryUtility.instantiateAccount("Stanislava");

    context.checking(new Expectations() {{
      allowing(connectionProvider).get();
      will(returnValue(connection));
    }});
    accountRepository.createAccount("Stanislava");

    Double originalAmount = accountRepository.getCurrentBalance(username);

    accountRepository.deposit(new Amount(username, amount));
    Double depositedAmount = accountRepository.getCurrentBalance(username) - originalAmount;

    assertThat(depositedAmount, is(equalTo(amount)));
  }

  @Test
  public void withdrawFunds() throws ValidationException {

    Double withdrawAmount = amount - 2.32;
    context.checking(new Expectations() {{
      allowing(connectionProvider).get();
      will(returnValue(connection));

      allowing(connectionProvider).get();
      will(returnValue(connection));
    }});
    accountRepository.createAccount("Stanislava");

    Double originalAmount = accountRepository.getCurrentBalance(username);

    accountRepository.deposit(new Amount(username, amount));
    Double depositedAmount = accountRepository.getCurrentBalance(username) - originalAmount;

    accountRepository.withdraw(new Amount(username, withdrawAmount));
    Double withdrawnAmount = (originalAmount + depositedAmount) - accountRepository.getCurrentBalance(username);

    assertThat(depositedAmount, is(equalTo(amount)));
    assertThat(withdrawnAmount, is(equalTo(withdrawAmount)));
  }
}
