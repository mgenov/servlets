package com.clouway.bank;

import com.clouway.bank.adapter.server.jetty.Jetty;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Demo {
  public static void main(String[] args) {
    Jetty jetty = new Jetty(8080);
    jetty.start();
  }
}
