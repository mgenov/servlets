package com.clouway.http.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class BankServer {
    private final Server server;
    private final ServletContextHandler context;

    public BankServer(int port, String path){
        server = new Server(port);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(path);
        server.setHandler(context);
    }

    public void addServlet(HttpServlet servlet, String url){
        context.addServlet(new ServletHolder(servlet), url);
    }

    public void start(){
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
