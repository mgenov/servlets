package com.clouway.adapter.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 26.05.16.
 */
public class VisitorsListener implements HttpSessionListener {
  ServletContext context = null;
  static int current = 0;

  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    current++;
    context = httpSessionEvent.getSession().getServletContext();
    context.setAttribute("currentUsers", current);
  }

  public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    current--;
    context.setAttribute("currentUsers", current);
  }
}
