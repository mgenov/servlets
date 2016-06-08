package com.clouway.adapter.persistence;

import com.clouway.core.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 09.06.16.
 */
public class PersistentFundsRepositoryTest {
  private ConnectionProvider connectionProvider = new FakeJdbcConnectionProvider();
  private PersistentFundsRepository fundsRepository = new PersistentFundsRepository(connectionProvider);
  private final String email = "admin@abv.bg";

  @Before
  public void cleanUp() {
    deleteAmount(email);
  }

  @Test
  public void deposit() throws Exception {
    fundsRepository.deposit(210.00, email);

    Double expected = 210.00;
    Double actual = fundsRepository.getBalance(email);
    assertEquals(expected, actual);
  }

  @Test
  public void getBalance() throws Exception {
    fundsRepository.deposit(290.00, email);

    Double expected = 290.00;
    Double actual = fundsRepository.getBalance(email);
    assertEquals(expected, actual);
  }

  @Test
  public void withdraw() throws Exception {
    fundsRepository.deposit(500.00, email);
    fundsRepository.withdraw(300.00, email);

    Double expected = 200.00;
    Double actual = fundsRepository.getBalance(email);
    assertEquals(expected, actual);
  }

  @Test
  public void notEnoughMoney() throws Exception {
    fundsRepository.deposit(0.00,email);

    boolean expected = false;
    boolean actual= fundsRepository.withdraw(20.00,email);

    assertEquals(expected,actual);
  }

  private void deleteAmount(String email) {
    Connection connection = connectionProvider.get();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("UPDATE users SET amount='0.00' WHERE email=?");
      statement.setString(1, email);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
