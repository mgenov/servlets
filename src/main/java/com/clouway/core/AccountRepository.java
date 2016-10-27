package com.clouway.core;

import java.util.Optional;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface AccountRepository {
  void register(Account account);

  Optional<Account> getByName(String name);
}

