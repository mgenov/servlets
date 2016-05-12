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
import java.net.MalformedURLException;
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
public class BankOperationsTest {
  @Rule
  public DatabaseConnectionRule connectionRule = new DatabaseConnectionRule("bank_test");
  @Mock
  TransactionValidator validator;
  private AccountRepository accountRepository;
  private Jetty jetty;
  private Connection connection;
  private AccountRepositoryUtility accountRepositoryUtility;
  private UserRepositoryUtility userRepositoryUtility;


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
    Double amount = 115.0d;
    String url = "http://localhost:8080/deposit?username=Stanislava&amount=" + amount;

    URL urlObj = new URL(url);

    HttpURLConnection connection=(HttpURLConnection) urlObj.openConnection();
    connection.setRequestMethod("POST");

    InputStream postIn = connection.getInputStream();
    String postResult = IOUtils.toString(postIn);

    assertThat(postResult, containsString(amount.toString()));

  }

  @Test
  public void withdrawSomeOfTheDepositedFunds() throws IOException {
    Double amount = 12.5d;
    String depositUrlAddress = "http://localhost:8080/deposit?username=Stanislava&amount=" + amount;

    Double withdrawAmount = amount-2.5;

    String withdrawUrlAddress = "http://localhost:8080/withdraw?username=Stanislava&amount="+withdrawAmount;


    URL depositUrl = new URL(depositUrlAddress);
    URL withdrawUrl = new URL(withdrawUrlAddress);

    HttpURLConnection connection = (HttpURLConnection) depositUrl.openConnection();
    connection.setRequestMethod("POST");

    InputStream depositIn = connection.getInputStream();
    String depositResult = IOUtils.toString(depositIn);

    HttpURLConnection withdrawConnection = (HttpURLConnection) withdrawUrl.openConnection();
    withdrawConnection.setRequestMethod("POST");

    InputStream withdrawIn = withdrawConnection.getInputStream();
    String withdrawResult = IOUtils.toString(withdrawIn);

    Double expectedAmount = 2.5;
    assertThat(withdrawResult, containsString(expectedAmount.toString()));
  }

  @Test
  public void invalidAmount() throws IOException {
    Double amount = 56.34;
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

    assertThat(result, containsString("incorrect value, has to have a positive number '.' and one or two digits afterwards"));
  }

  @Test
  public void negativeDeposit() throws IOException {
    Double amount = 634.20d;
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

    assertThat(result, containsString("incorrect value, has to have a positive number '.' and one or two digits afterwards"));
  }
}
