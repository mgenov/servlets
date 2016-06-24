package com.clouway.links;

import com.clouway.adapter.server.jetty.Jetty;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Demo {
  public static void main(String[] args) {
    Jetty servlet = new Jetty(8080);
    servlet.start();
  }
}
