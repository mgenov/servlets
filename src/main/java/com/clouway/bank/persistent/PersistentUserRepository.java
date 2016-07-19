package com.clouway.bank.persistent;

import com.clouway.bank.core.DataStore;
import com.clouway.bank.core.RowFetcher;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.core.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  private DataStore dataStore;

  public PersistentUserRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  /**
   * Registers the user
   *
   * @param user the user object
   */
  @Override
  public void register(User user) {
    String query = "INSERT INTO users(username, password) VALUES(?, ?);";

    if (getUserById(user.id) != null) {
      throw new ValidationException("username is taken");
    }
    dataStore.executeQuery(query, user.id, user.password);
  }


  /**
   * Finds user by it's name
   *
   * @param userId the user name
   * @return the user
   */
  public User getUserById(String userId) {
    String query = "SELECT * FROM users WHERE username = ?;";

    RowFetcher<User> rowFetcher = rs -> new User(rs.getString(1), rs.getString(2));
    List<User> userList = new ArrayList<>();
    userList = dataStore.fetchRows(query, rowFetcher, userId);
    if (userList.isEmpty()) {
      return null;
    }

    return userList.get(0);
  }
}
