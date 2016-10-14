package com.clouway;

import com.clouway.http.server.JettyServer;
import com.clouway.http.servlets.MainPageServlet;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class App {
    public static void main(String[] args) {
        JettyServer server = new JettyServer(8080);
        server.initHandler("/");
        server.addServlet(new MainPageServlet(), "/");
        server.start();
    }
}
