package com.clouway.buttons;

import com.clouway.adapter.servlet.jetty.Jetty;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Demo {
  public static void main(String[] args) {
    Jetty jetty = new Jetty(8080);
    jetty.start();
  }
}
