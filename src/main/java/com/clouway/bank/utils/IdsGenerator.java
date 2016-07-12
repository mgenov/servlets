package com.clouway.bank.utils;

import com.clouway.bank.core.Generator;

import java.util.UUID;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class IdsGenerator implements Generator {
  @Override
  public String generateId() {
    return UUID.randomUUID().toString();
  }
}
