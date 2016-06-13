package com.clouway.bank;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepositoryTest {
  private PersistentUserRepository persistentUserRepository;

  @Before
  public void setUp(){
    persistentUserRepository = new PersistentUserRepository();
  }

  @Test
  public void findRegisteredUser(){

    User user = persistentUserRepository.findByName("Krasimir");
    assertThat(user.password, is(equalTo("123456")));
  }
}
