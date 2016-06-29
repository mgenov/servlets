package com.clouway.bank.http;

import com.clouway.utility.Template;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Catching exceptions and printing messages to an Error page
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class HttprequestErrorReporter implements Filter {
  private Template template;

  /**
   * Sets the template for the displaying of the message
   *
   * @param template
   */
  public HttprequestErrorReporter(Template template) {
    this.template = template;
  }

  /**
   * destroys the filter context
   */
  public void destroy() {
  }


  /**
   * catches exceptions if they occur and reports the problem to the user
   *
   * @param req   the request
   * @param resp  the response
   * @param chain the filter chain
   * @throws ServletException
   * @throws IOException
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    try {
      chain.doFilter(req, resp);
    } catch (Exception ex) {
      template.loadFromFile("web/WEB-INF/pages/ErrorPage.html");
      template.put("errorMessage", ex.getMessage() + ". Sorry, if there is some mistake please contact us.");
      resp.getWriter().println(template.evaluate());
    }
  }

  /**
   * initiates the filter context
   *
   * @param config
   * @throws ServletException
   */
  public void init(FilterConfig config) throws ServletException {
  }

}