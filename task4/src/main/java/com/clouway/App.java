package com.clouway;

import com.clouway.http.server.JettyServer;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class App {
    public static void main(String[] args) {
        JettyServer server = new JettyServer();
        server.setPort(8080);
        server.initHandler("/main","src/main/webapp");

        server.start();
    }
}
