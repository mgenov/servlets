package com.clouway.bank.persistence;

import com.clouway.bank.adapter.jdbc.ConnectionProvider;
import com.clouway.bank.core.ConnectionException;
import com.clouway.bank.core.Provider;
import com.clouway.bank.core.User;
import com.clouway.bank.core.UserRepository;
import com.clouway.bank.adapter.jdbc.db.persistence.PersistentUserRepository;
import com.clouway.bank.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class PersistenceUserRepositoryTest {
  private Provider<Connection> connection;

  @Before
  public void setUp() throws Exception {
    connection = new ConnectionProvider("jdbc:postgresql://localhost/test", "postgres", "clouway.com");
  }

  @Test
  public void registerUser() throws Exception {
    UserRepository repository = new PersistentUserRepository(connection);
    User user = new User("Ivan", "ivan@abv.bg", "1234333333");

    repository.register(user);
    User actual = repository.findByEmail("ivan@abv.bg");

    assertThat(actual, is(user));
  }

  @Test
  public void findNoRegisteredUser() throws Exception {
    UserRepository repository = new PersistentUserRepository(connection);
    User actual = repository.findByEmail("aaaa@abv.bg");

    assertThat(actual, is(equalTo(null)));
  }
}
