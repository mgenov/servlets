package com.clouway.http.servlets;

import com.clouway.core.ServletResponseWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
class HtmlServletResponseWriter implements ServletResponseWriter {

  @Override
  public void writeResponse(HttpServletResponse response, String htmlContent) throws IOException {
    response.setContentType("text/html");
    response.setStatus(HttpURLConnection.HTTP_OK);
    PrintWriter writer = response.getWriter();
    writer.print(htmlContent);
    writer.flush();
  }
}
