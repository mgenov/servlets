package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountHistoryRepository;
import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.ConnectionProvider;
import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentAccountHistoryRepositoryTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  AccountHistoryRepository historyRepository;
  @Mock
  ConnectionProvider connectionProvider;

  @Mock
  Connection connection;

  @Mock
  DataStore dataStore;


  @Before
  public void setUp() {
    historyRepository = new PersistentAccountHistoryRepository(dataStore);
  }


  @Test
  public void getAccountHistory() throws SQLException {
    List<AccountRecord> records = new ArrayList<>();
    records.add(new AccountRecord(100L, "Kristian", "deposit", 14.5D));
    records.add(new AccountRecord(170L, "Kristian", "withdraw", 4.5D));

    context.checking(new Expectations() {{
      oneOf(dataStore).fetchRows(with(equalTo("SELECT date, username, operation, amount FROM account_history WHERE username=? ORDER BY date LIMIT ? OFFSET ?;")), with(any(RowFetcher.class)), with(equalTo(new Object[]{"Kristian", 20, 0})));
      will(returnValue(records));

    }});
    List<AccountRecord> accountRecords = historyRepository.getAccountRecords("Kristian", 0, 20);


    List<AccountRecord> expectedRecords = new ArrayList<>();
    expectedRecords.add(new AccountRecord(100L, "Kristian", "deposit", 14.5D));
    expectedRecords.add(new AccountRecord(170L, "Kristian", "withdraw", 4.5D));

    assertThat(accountRecords, is(equalTo(expectedRecords)));
  }

}