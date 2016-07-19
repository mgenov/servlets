package com.clouway.bank.persistent;

import com.clouway.bank.core.AccountRecord;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.User;
import com.clouway.bank.utils.FakeConnectionProvider;
import com.clouway.bank.utils.UserRepositoryUtility;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DatabaseHelperTest {
  private DatabaseHelper databaseHelper;
  private FakeConnectionProvider connectionProvider;
  private UserRepositoryUtility userRepositoryUtility;

  @Before
  public void setUp() {
    connectionProvider = new FakeConnectionProvider();
    databaseHelper = new DatabaseHelper(connectionProvider);
    userRepositoryUtility = new UserRepositoryUtility(connectionProvider.get());
    userRepositoryUtility.clearUsersTable();
  }

  @Test
  public void insertDataIntoTable() {
    User petar = new User("petar1", "123456");
    String insertUser = "INSERT INTO users(username, password) VALUES(?, ?);";
    String getUser = "SELECT * FROM users WHERE username = ?;";
    RowFetcher<User> rowFetcher = new RowFetcher<User>() {
      @Override
      public User fetchRow(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("password"));
      }
    };
    databaseHelper.executeQuery(insertUser, petar.id, petar.password);

    List<User> petarList = databaseHelper.fetchRows(getUser, rowFetcher, petar.id);

    assertThat(petarList.get(0), is(equalTo(petar)));
  }

  @Test
  public void updateDataIntoTable() {
    User mancho = new User("mancho", "123456");
    String insertUser = "INSERT INTO users(username, password) VALUES(?, ?);";
    String updateUser = "UPDATE users SET password=? WHERE username=?";
    String getUser = "SELECT * FROM users WHERE username = ?;";

    RowFetcher<User> rowFetcher = new RowFetcher<User>() {
      @Override
      public User fetchRow(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("password"));
      }
    };

    databaseHelper.executeQuery(insertUser, mancho.id, mancho.password);
    databaseHelper.executeQuery(updateUser, "qwerty", mancho.id);

    List<User> manchoList = databaseHelper.fetchRows(getUser, rowFetcher, mancho.id);

    User expectedManchoUpdated = new User("mancho", "qwerty");
    assertThat(manchoList.get(0), is(equalTo(expectedManchoUpdated)));
  }

  @Test
  public void deleteDataFromDatabase() {
    User ivailo = new User("ivailo", "asdfgh");
    String insertUser = "INSERT INTO users(username, password) VALUES(?, ?);";
    String deleteUser = "DELETE FROM users WHERE username=?;";
    String getUser = "SELECT * FROM users WHERE username = ?;";

    RowFetcher<User> rowFetcher = new RowFetcher<User>() {
      @Override
      public User fetchRow(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("password"));
      }
    };

    databaseHelper.executeQuery(insertUser, ivailo.id, ivailo.password);
    databaseHelper.executeQuery(deleteUser, ivailo.id);

    List<User> ivailoList = databaseHelper.fetchRows(getUser, rowFetcher, ivailo.id);

    assertThat(ivailoList.size(), is(equalTo(0)));
  }

  @Test
  public void getMultipleRecordsFromTable() {
    User stanislava = new User("stanislava", "123456");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    Long depositTime = new Timestamp(calendar.getTimeInMillis()).getTime();
    calendar.add(Calendar.DAY_OF_MONTH, 12);
    Long withdrawTime = new Timestamp(calendar.getTimeInMillis()).getTime();
    AccountRecord stanislavaFirstDeposit = new AccountRecord(depositTime, "stanislava", "deposit", 12D);
    AccountRecord stanislavaFirstWithdraw = new AccountRecord(withdrawTime, "stanislava", "withdraw", 2.5D);

    String insertUser = "INSERT INTO users(username, password) VALUES(?, ?);";
    String insertAccountRecord = "INSERT INTO account_history(date, username, operation, amount) VALUES(?, ?, ?, ?)";
    String getAccountTransactionHistory = "SELECT date, username, operation, amount FROM account_history WHERE username=?";
    String truncateHistory = "truncate table account_history;";
    RowFetcher<AccountRecord> rowFetcher = rs -> new AccountRecord(rs.getTimestamp("date").getTime(), rs.getString("username"), rs.getString("operation"), rs.getDouble("amount"));

    databaseHelper.executeQuery(insertUser, stanislava.id, stanislava.password);
    databaseHelper.executeQuery(insertAccountRecord, new Timestamp(stanislavaFirstDeposit.date), stanislavaFirstDeposit.userId, stanislavaFirstDeposit.operation, stanislavaFirstDeposit.amount);
    databaseHelper.executeQuery(insertAccountRecord, new Timestamp(stanislavaFirstWithdraw.date), stanislavaFirstWithdraw.userId, stanislavaFirstWithdraw.operation, stanislavaFirstWithdraw.amount);

    List<AccountRecord> stanislavaExpectedHistory = new ArrayList<>();
    stanislavaExpectedHistory.add(stanislavaFirstDeposit);
    stanislavaExpectedHistory.add(stanislavaFirstWithdraw);

    List<AccountRecord> stanislavaActualHistory = databaseHelper.fetchRows(getAccountTransactionHistory, rowFetcher, stanislava.id);
    databaseHelper.executeQuery(truncateHistory);
    assertThat(stanislavaActualHistory, is(equalTo(stanislavaExpectedHistory)));
  }

  @Test
  public void countRecords() {
    User petar = new User("petar1", "123456");
    User stanislava = new User("stanislava", "123456");
    String insertUser = "INSERT INTO users(username, password) VALUES(?, ?);";
    String countUsers = "SELECT count(*) FROM users;";

    RowFetcher<Integer> rowFetcher = rs -> rs.getInt(1);

    databaseHelper.executeQuery(insertUser, petar.id, petar.password);
    databaseHelper.executeQuery(insertUser, stanislava.id, stanislava.password);

    List<Integer> numberOfUsersList = databaseHelper.fetchRows(countUsers, rowFetcher);

    assertThat(numberOfUsersList.get(0), is(equalTo(2)));
  }
}