package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Amount;
import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@RunWith(Parameterized.class)
public class PersistentAccountRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");

  @Mock
  DataStore dataStore;

  private AccountRepository accountRepository;
  private Connection connection;
  private AccountRepositoryUtility accountRepositoryUtility;
  private UserRepositoryUtility userRepositoryUtility;
  private String username = "Stanislava";
  private Double amount;

  public PersistentAccountRepositoryTest(Double amount) {
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
    accountRepository = new PersistentAccountRepository(dataStore);
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
    List<Double> queryResult = new ArrayList<>();
    queryResult.add(0D);

    List<Double> depositedResult = new ArrayList<>();
    depositedResult.add(amount);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO account(username, balance) VALUES(?, ?);", new Object[]{"Stanislava", 0D});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT balance FROM account WHERE username=?")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Stanislava"})));
      will(returnValue(queryResult));

      oneOf(dataStore).executeQuery("UPDATE account SET balance=balance+? WHERE username=?", new Object[]{amount, "Stanislava"});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT balance FROM account WHERE username=?")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Stanislava"})));
      will(returnValue(depositedResult));
    }});
    accountRepository.createAccount("Stanislava");

    Double originalAmount = accountRepository.getCurrentBalance(username);

    Double afterDeposit = accountRepository.deposit(new Amount(username, amount));
    Double depositedAmount = afterDeposit - originalAmount;

    assertThat(depositedAmount, is(equalTo(amount)));
  }

  @Test
  public void withdrawFunds() throws ValidationException {
    Double withdrawAmount = amount - 2.32;

    List<Double> queryResult = new ArrayList<>();
    queryResult.add(amount);

    List<Double> secondQueryResult = new ArrayList<>();
    secondQueryResult.add(amount - withdrawAmount);

    context.checking(new Expectations() {{
      oneOf(dataStore).executeQuery("INSERT INTO account(username, balance) VALUES(?, ?);", new Object[]{"Stanislava", 0D});

      oneOf(dataStore).executeQuery("UPDATE account SET balance=balance+? WHERE username=?", new Object[]{amount, "Stanislava"});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT balance FROM account WHERE username=?")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Stanislava"})));
      will(returnValue(queryResult));

      oneOf(dataStore).fetchRows(with(equalTo("SELECT balance FROM account WHERE username=?")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Stanislava"})));
      will(returnValue(queryResult));

      oneOf(dataStore).executeQuery("UPDATE account SET balance=balance-? WHERE username=?", new Object[]{withdrawAmount, "Stanislava"});

      oneOf(dataStore).fetchRows(with(equalTo("SELECT balance FROM account WHERE username=?")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Stanislava"})));
      will(returnValue(secondQueryResult));
    }});
    accountRepository.createAccount("Stanislava");

    Double originalAmount = 0D;

    Double afterDeposit = accountRepository.deposit(new Amount(username, amount));
    Double depositedAmount = afterDeposit - originalAmount;

    Double afterWithdraw = accountRepository.withdraw(new Amount(username, withdrawAmount));
    Double withdrawnAmount = depositedAmount - afterWithdraw;

    assertThat(depositedAmount, is(equalTo(amount)));
    assertThat(withdrawnAmount, is(equalTo(withdrawAmount)));
  }
}
