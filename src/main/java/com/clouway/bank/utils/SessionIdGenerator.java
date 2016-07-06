package com.clouway.bank.utils;

import com.clouway.bank.core.IdGenerator;

import java.util.UUID;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class SessionIdGenerator implements IdGenerator {
  @Override
  public String generateId() {
    return UUID.randomUUID().toString();
  }
}
