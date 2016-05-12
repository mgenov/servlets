package com.clouway.bank.core;

import com.clouway.bank.persistent.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class SessionCleaner {
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private final int cleanInterval;
  private BankCalendar bankCalendar;
  private ConnectionManager connectionManager;

  public SessionCleaner(BankCalendar bankCalendar, int intervalInSeconds, ConnectionManager connectionManager) {

    this.bankCalendar = bankCalendar;
    this.cleanInterval = intervalInSeconds;
    this.connectionManager = connectionManager;
  }

  public void clearDeadSessions() {
    final Runnable cleaner = new Runnable() {
      public void run() {
        String query = "DELETE  FROM login WHERE expirationtime < ?;";
        Connection connection = null;
        try {
          connection = connectionManager.get();
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setTimestamp(1, new Timestamp(bankCalendar.getCurrentTime()));
          preparedStatement.execute();
        } catch (SQLException e) {
          e.printStackTrace();
        } finally {

          try {
            if (connection != null) {
              connection.close();
            }

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    };


    final ScheduledFuture<?> beeperHandle =
            scheduler.scheduleAtFixedRate(cleaner, cleanInterval, cleanInterval, SECONDS);


  }
}
