package com.clouway.adapter.persistence;

import com.clouway.adapter.http.PerRequestConnectionProvider;
import com.clouway.core.ConnectionProvider;
import com.clouway.core.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 03.06.16.
 */
public class PersistentUserRepositoryTest {
  private ConnectionProvider connectionProvider = new FakeJdbcConnectionProvider();
  private PersistentUserRepository userRepository = new PersistentUserRepository(connectionProvider);

  @Before
  public void cleanUp() {
    userRepository.deleteAll();
  }


  @Test
  public void registerNewUser() throws Exception {

    User user = new User("Georgi", "georgi", "gosho@abv.bg");
    userRepository.register(user);

    User expected = userRepository.findByEmail("gosho@abv.bg");

    assertThat(user, is(expected));
  }

  @Test
  public void authenticate() throws Exception {
    String email = "admin@abv.bg";
    String password = "adminadmin";
    String username = "GlobalAdmin";

    userRepository.register(new User(username, password, email));

    boolean isAuthenticated = userRepository.authenticate(email, password);

    boolean expected = true;

    assertThat(expected, is(equalTo(isAuthenticated)));
  }
}


