package com.clouway.adapter.http;

import com.clouway.adapter.persistence.PersistentSessionRepository;
import com.clouway.adapter.persistence.PersistentUserRepository;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 26.05.16.
 */
public class HttpServletContextListener implements ServletContextListener {
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    servletContext.addFilter("ConnectionFilter", new ConnectionFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    servletContext.addFilter("SecurityFilter", new SecurityFilter(new PersistentSessionRepository(new PerRequestConnectionProvider()))).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/useraccount");
    servletContext.addServlet("login", new LoginPage()).addMapping("/login");
    servletContext.addServlet("logincontroller", new LoginController(new PersistentUserRepository(new PerRequestConnectionProvider()), new PersistentSessionRepository(new PerRequestConnectionProvider()))).addMapping("/logincontroller");
    servletContext.addServlet("registercontroller", new RegisterController(new PersistentUserRepository(new PerRequestConnectionProvider()))).addMapping("/registercontroller");
    servletContext.addServlet("register", new RegisterPage()).addMapping("/register");
    servletContext.addServlet("logoutcontroller",new LogoutController(new PersistentSessionRepository(new PerRequestConnectionProvider()))).addMapping("/logoutcontroller");
    servletContext.addServlet("useraccount", new UserAccount()).addMapping("/useraccount");
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
