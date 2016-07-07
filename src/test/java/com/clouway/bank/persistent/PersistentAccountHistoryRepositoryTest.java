package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountHistoryRepository;
import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.ConnectionProvider;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
  PreparedStatement preparedStatement;

  @Mock
  ResultSet resultSet;

  @Before
  public void setUp() {
    historyRepository = new PersistentAccountHistoryRepository(connectionProvider);
  }


  @Test
  public void getAccountHistory() throws SQLException {
    context.checking(new Expectations() {{
      oneOf(connectionProvider).get();
      will(returnValue(connection));

      oneOf(connection).prepareStatement("SELECT date, username, operation, amount FROM account_history WHERE username=? ORDER BY date LIMIT ? OFFSET ?;");
      will(returnValue(preparedStatement));

      oneOf(preparedStatement).setString(1, "Kristian");

      oneOf(preparedStatement).setInt(2, 20);

      oneOf(preparedStatement).setInt(3, 0);

      oneOf(preparedStatement).executeQuery();
      will(returnValue(resultSet));

      oneOf(resultSet).next();
      will(returnValue(true));

      oneOf(resultSet).getTimestamp(1);
      will(returnValue(new Timestamp(100)));

      oneOf(resultSet).getString(2);
      will(returnValue("Kristian"));

      oneOf(resultSet).getString(3);
      will(returnValue("deposit"));

      oneOf(resultSet).getDouble(4);
      will(returnValue(14.5));

      oneOf(resultSet).next();
      will(returnValue(true));

      oneOf(resultSet).getTimestamp(1);
      will(returnValue(new Timestamp(170)));

      oneOf(resultSet).getString(2);
      will(returnValue("Kristian"));

      oneOf(resultSet).getString(3);
      will(returnValue("withdraw"));

      oneOf(resultSet).getDouble(4);
      will(returnValue(4.5));

      oneOf(resultSet).next();
      will(returnValue(false));

      oneOf(resultSet).close();

      oneOf(preparedStatement).close();
    }});
    List<AccountRecord> accountRecords = historyRepository.getAccountRecords("Kristian", 0, 20);


    List<AccountRecord> expectedRecords = new ArrayList<>();
    expectedRecords.add(new AccountRecord(100L, "Kristian", "deposit", 14.5D));
    expectedRecords.add(new AccountRecord(170L, "Kristian", "withdraw", 4.5D));

    assertThat(accountRecords, is(equalTo(expectedRecords)));
  }

}