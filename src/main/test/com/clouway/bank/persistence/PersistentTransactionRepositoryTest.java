package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentTransactionRepository;
import com.clouway.bank.core.Account;
import com.clouway.bank.core.AccountRepository;
import com.clouway.bank.core.CurrentDate;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.Transaction;
import com.clouway.bank.core.TransactionRepository;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistentTransactionRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private CurrentDate date;

  @Mock
  private AccountRepository accountRepository;

  private Provider<Connection> provider;
  private PreparedStatement statement;

  private TransactionRepository transactionRepository;

  private final Account account = new Account("user@domain.com", 50.00);
  private final long currentDate = 1212121212;

  @Before
  public void setUp() throws Exception {
    provider = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");

    transactionRepository = new PersistentTransactionRepository(accountRepository, provider, date);

    statement = provider.get().prepareStatement("truncate table transactions;");
    statement.executeUpdate();
  }

  @After
  public void tearDown() throws Exception {
    statement.close();
  }

  @Test
  public void updateHistory() throws Exception {
    context.checking(new Expectations() {{
      oneOf(date).getCurrentDate();
      will(returnValue(currentDate));

      oneOf(accountRepository).findByEmail(account.getEmail());
      will(returnValue(account));
    }});

    transactionRepository.updateHistory(account.email, "deposit", 10.00);
    Double depositAmount = 10.00;

    List<Transaction> expected = Lists.newArrayList(new Transaction(currentDate, account.email, "deposit", depositAmount, 50.00));
    List<Transaction> actual = transactionRepository.getTransactions(account.email, 1, 0);

    assertThat(actual, is(expected));
  }

  @Test
  public void insertManyTransactions() throws Exception {
    final long currentDate = 123123123;
    Double depositAmount = 20.00;
    Double withdrawAmount = 20.00;

    final Transaction transaction1 = new Transaction(currentDate, account.email, "deposit", depositAmount, 50.00);
    final Transaction transaction2 = new Transaction(currentDate, account.email, "withdraw", withdrawAmount, 50.00);

    context.checking(new Expectations() {{
      oneOf(date).getCurrentDate();
      will(returnValue(currentDate));

      oneOf(accountRepository).findByEmail(account.email);
      will(returnValue(account));

      oneOf(date).getCurrentDate();
      will(returnValue(currentDate));

      oneOf(accountRepository).findByEmail(account.email);
      will(returnValue(account));
    }});

    transactionRepository.updateHistory(account.email, "deposit", depositAmount);
    transactionRepository.updateHistory(account.email, "withdraw", withdrawAmount);

    List<Transaction> expected = Lists.newArrayList(transaction1, transaction2);
    List<Transaction> actual = transactionRepository.getTransactions(account.email, 2, 0);

    assertThat(actual, is(expected));
  }
}
