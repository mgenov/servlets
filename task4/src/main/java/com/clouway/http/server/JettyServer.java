package com.clouway.http.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class JettyServer {
    private Server server;
    private WebAppContext context = new WebAppContext();

    public void setPort(int port) {
        server = new Server(port);
    }

    public void initHandler(String path, String war) {
//        context.setDescriptor("../webapp/WEB-INF/web.xml");

        context.setResourceBase(".");
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
//        context.setResourceBase("../test-jetty-webapp/src/main/webapp");
        context.setContextPath("/");


//        context.setContextPath("/");
//        context.setWar(jetty_home+"/webapps/test.war");
        server.setHandler(context);


    }

    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
