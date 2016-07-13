package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentAccountRepository;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Date;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.TransactionHistory;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentAccountRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private Date date = context.mock(Date.class);

  private Provider<Connection> provider;
  private PreparedStatement statement;

  @Before
  public void setUp() throws Exception {
    provider = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");

    statement = provider.get().prepareStatement("truncate table accounts;");
    statement.executeUpdate();
  }

  @After
  public void tearDown() throws Exception {
    statement.close();
  }

  @Test
  public void createAccount() throws Exception {
    AccountRepository repository = new PersistentAccountRepository(provider, date);
    Account account = new Account("user@domain.com", 0.0);

    repository.createAccount(account);
    Account actual = repository.findByEmail("user@domain.com");

    assertThat(actual, is(account));
  }

  @Test
  public void deposit() throws Exception {
    AccountRepository repository = new PersistentAccountRepository(provider, date);
    Account account = new Account("user@domain.com", 0.0);

    repository.createAccount(account);
    repository.deposit(account.email, 2.2);

    Account actual = repository.findByEmail(account.email);

    assertThat(actual.getBalance(), is(2.2));
  }

  @Test
  public void withdraw() throws Exception {
    AccountRepository repository = new PersistentAccountRepository(provider, date);
    Account account = new Account("user@domain.com", 12.00);

    repository.createAccount(account);
    repository.withdraw(account.email, 12.00);

    Account actual = repository.findByEmail(account.email);

    assertThat(actual.getBalance(), is(0.0));
  }

  @Test
  public void getBalance() throws Exception {
    AccountRepository repository = new PersistentAccountRepository(provider, date);
    Account account = new Account("user@domain.com", 50.00);

    repository.createAccount(account);

    Double actual = repository.getBalance(account.email);

    assertThat(actual, is(50.00));
  }

  @Test
  public void updateTransactionsHistory() throws Exception {
    final AccountRepository accountRepository = new PersistentAccountRepository(provider, date);
    final Account account = new Account("user@domain.com", 50.00);

    final long currentDate = convertStringToLong("12:12:2012");
    accountRepository.createAccount(account);

    accountRepository.deposit(account.email, 10.00);

    context.checking(new Expectations() {{
      allowing(date).getCurrentDate();
      will(returnValue(currentDate));
    }});

    accountRepository.updateHistory(account.email, "deposit");
    List<TransactionHistory> actual = accountRepository.getTransactionsHistory();
    List<TransactionHistory> expected = Lists.newArrayList(new TransactionHistory(currentDate, account.email, "deposit", 60));

    assertThat(actual, is(expected));
  }

  private long convertStringToLong(String dateAsString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:mm:yyyy");
    java.util.Date date = null;
    try {
      date = simpleDateFormat.parse(dateAsString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date.getTime();
  }
}
