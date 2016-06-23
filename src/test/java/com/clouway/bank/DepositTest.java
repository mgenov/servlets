package com.clouway.bank;

import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.TransactionValidator;
import com.clouway.bank.http.Jetty;
import com.clouway.bank.persistent.PerRequestConnectionProvider;
import com.clouway.bank.persistent.PersistentAccountRepository;
import com.clouway.bank.utils.AccountRepositoryUtility;
import com.clouway.bank.utils.DatabaseConnectionRule;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.apache.commons.io.IOUtils;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@RunWith(Parameterized.class)
public class DepositTest {
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  @Mock
  TransactionValidator validator;
  private AccountRepository accountRepository;
  private Jetty jetty;
  private Connection connection;
  private AccountRepositoryUtility accountRepositoryUtility;
  private UserRepositoryUtility userRepositoryUtility;
  private Double amount;

  public DepositTest(java.lang.Double amount) {
    this.amount = amount;
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {10.0}, {110.0}, {243.0}, {315.0}
    });
  }

  @Before
  public void setUp() throws SQLException {
    jetty = new Jetty(8080, "bank_test");
    accountRepository = new PersistentAccountRepository(new PerRequestConnectionProvider(), validator);
    jetty.start();
    connection = connectionRule.getConnection();
    accountRepositoryUtility = new AccountRepositoryUtility(connection);
    accountRepositoryUtility.clearAccountTable();
    userRepositoryUtility = new UserRepositoryUtility(connection);
    userRepositoryUtility.clearUsersTable();
    userRepositoryUtility.registerUser("Stanislava", "123456");
    accountRepositoryUtility.instantiateAccount("Stanislava");
  }

  @After
  public void TearDown() throws SQLException {
    accountRepositoryUtility.clearAccountTable();
    userRepositoryUtility.clearUsersTable();
    connection.close();
    jetty.stop();
  }

  @Test
  public void depositFunds() throws Exception {
    String url = "http://localhost:8080/deposit?amount=" + amount;

    URL urlObj = new URL(url);

    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
    connection.setRequestMethod("POST");

    InputStream in = connection.getInputStream();
    String result = IOUtils.toString(in);

    assertThat(result, containsString(amount.toString()));

  }

  @Test
  public void multipleDeposits() throws Exception {
    String url = "http://localhost:8080/deposit?username=Stanislava&amount=" + amount;

    URL urlObj = new URL(url);

    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
    connection.setRequestMethod("POST");

    InputStream input = connection.getInputStream();
    String firstResponse = IOUtils.toString(input);
    connection.disconnect();

    HttpURLConnection secondConnection = (HttpURLConnection) urlObj.openConnection();
    secondConnection.setRequestMethod("POST");

    InputStream in = secondConnection.getInputStream();
    String result = IOUtils.toString(in);
    secondConnection.disconnect();

    assertThat(result, containsString(String.valueOf(amount * 2)));
  }

  @Test
  public void invalidAmount() throws IOException {
    String url = "http://localhost:8080/deposit?username=Stanislava&amount=e" + amount;

    URL urlObj = new URL(url);

    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
    connection.setRequestMethod("POST");

    InputStream input = connection.getInputStream();
    String firstResponse = IOUtils.toString(input);
    connection.disconnect();

    HttpURLConnection secondConnection = (HttpURLConnection) urlObj.openConnection();
    secondConnection.setRequestMethod("POST");

    InputStream in = secondConnection.getInputStream();
    String result = IOUtils.toString(in);
    secondConnection.disconnect();

    assertThat(result, containsString("incorrect amount, has to have a positive number '.' and at least one digit afterwards"));
  }

  @Test
  public void negativeDeposit() throws IOException {
    String url = "http://localhost:8080/deposit?username=Stanislava&amount=-" + amount;

    URL urlObj = new URL(url);

    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
    connection.setRequestMethod("POST");

    InputStream input = connection.getInputStream();
    String firstResponse = IOUtils.toString(input);
    connection.disconnect();

    HttpURLConnection secondConnection = (HttpURLConnection) urlObj.openConnection();
    secondConnection.setRequestMethod("POST");

    InputStream in = secondConnection.getInputStream();
    String result = IOUtils.toString(in);
    secondConnection.disconnect();

    assertThat(result, containsString("incorrect amount, has to have a positive number '.' and at least one digit afterwards"));
  }
}
