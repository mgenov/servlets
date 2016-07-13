package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentAccountRepository;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.Provider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentAccountRepositoryTest {
  private Provider<Connection> provider;
  private PreparedStatement statement;

  private AccountRepository accountRepository;
  Account account = new Account("user@domain.com", 0.0);

  @Before
  public void setUp() throws Exception {
    provider = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");

    accountRepository = new PersistentAccountRepository(provider);

    statement = provider.get().prepareStatement("truncate table accounts;");
    statement.executeUpdate();
  }

  @After
  public void tearDown() throws Exception {
    statement.close();
  }

  @Test
  public void createAccount() throws Exception {
    accountRepository.createAccount(account);
    Account actual = accountRepository.findByEmail("user@domain.com");

    assertThat(actual, is(account));
  }

  @Test
  public void deposit() throws Exception {
    accountRepository.createAccount(account);
    accountRepository.deposit(account.email, 2.2);

    Account actual = accountRepository.findByEmail(account.email);

    assertThat(actual.getBalance(), is(2.2));
  }

  @Test
  public void withdraw() throws Exception {
    accountRepository.createAccount(account);
    accountRepository.withdraw(account.email, 12.00);

    Account actual = accountRepository.findByEmail(account.email);

    assertThat(actual.getBalance(), is(0.0));
  }

  @Test
  public void getBalance() throws Exception {
    accountRepository.createAccount(account);

    Double actual = account.getBalance();

    assertThat(actual, is(00.00));
  }
}
