package com.clouway.core;

import java.util.UUID;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public class RandomGeneratorImpl implements RandomGenerator {
  public String generateUUID() {
    return UUID.randomUUID().toString();
  }
}
