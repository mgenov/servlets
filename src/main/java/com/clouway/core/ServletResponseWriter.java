package com.clouway.core;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ServletResponseWriter is a writer which write content to the {@link HttpServletResponse}.
 *
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public interface ServletResponseWriter {

  /**
   * Writes response to the client
   *
   * @param response the object used to write the response
   * @param htmlPage the html string to be writen in the response
   */
  void writeResponse(HttpServletResponse response, String htmlPage) throws IOException;
}
