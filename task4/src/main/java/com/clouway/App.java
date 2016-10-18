package com.clouway;

import com.clouway.http.server.JettyServer;
import com.clouway.http.servlets.MainPageServlet;

import javax.servlet.ServletContext;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class App {
    public static void main(String[] args) {
        JettyServer server = new JettyServer();
        server.setPort(8080);
        server.initHandler("/","src/main/webapp");
        server.start();
    }
}
