package com.clouway;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clouway on 19.05.16.
 */
public class LoginFilter implements Filter {

  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    Map<String, String> userDB = new HashMap<String, String>() {{
      put("admin", "admin");
      put("kristiyan", "123456");
    }};


//    String regName=servletRequest.getParameter("regname");
//    String regPassword=servletRequest.getParameter("regpassword");
//
//    if(regName!=null && regPassword!=null){
//      userDB.put(regName,regPassword);
//    }

    String username = servletRequest.getParameter("name");
    String password = servletRequest.getParameter("password");

    if (userDB.containsKey(username) && userDB.get(username).equals(password)) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      RequestDispatcher rd = servletRequest.getRequestDispatcher("login");
      servletRequest.setAttribute("errorMessage","<h5>Wrong username or password!</h5>");
      rd.forward(servletRequest, servletResponse);
    }
  }

  public void destroy() {

  }
}
