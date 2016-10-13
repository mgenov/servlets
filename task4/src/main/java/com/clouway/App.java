package com.clouway;

import com.clouway.http.server.BankServer;
import com.clouway.http.servlets.MainPageServlet;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class App {
    public static void main(String[] args) {
        BankServer server = new BankServer(8080,"/");
        server.addServlet(new MainPageServlet(), "/");
        server.start();
    }
}
