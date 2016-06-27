package com.clouway.adapter.servlet.jetty;

import com.clouway.buttons.FirstPageServlet;
import com.clouway.buttons.RadioButtonsServlet;
import com.clouway.buttons.SecondPageServlet;
import com.clouway.buttons.ThirdPageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Jetty {
    private final Server server;

    public Jetty(int port) {
        this.server = new Server(port);
    }

    public void start() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new ServletContextListener() {

            public void contextInitialized(ServletContextEvent servletContextEvent) {
                ServletContext servletContext = servletContextEvent.getServletContext();

                servletContext.addServlet("menu", new RadioButtonsServlet()).addMapping("/menu");
                servletContext.addServlet("first", new FirstPageServlet()).addMapping("/first");
                servletContext.addServlet("second", new SecondPageServlet()).addMapping("/second");
                servletContext.addServlet("third", new ThirdPageServlet()).addMapping("/third");
            }

            public void contextDestroyed(ServletContextEvent servletContextEvent) {

            }
        });

        server.setHandler(context);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
