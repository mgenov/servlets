package com.clouway.http.servlets;

import com.clouway.core.HtmlTemplate;
import com.clouway.core.ServletPageRenderer;
import com.google.common.io.ByteStreams;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
class HtmlServletPageRenderer implements ServletPageRenderer {

  @Override
  public void renderPage(String pageName, Map<String, Object> params, HttpServletResponse response) throws IOException {
    try {
      String page = new String(ByteStreams.toByteArray(HtmlServletPageRenderer.class.getResourceAsStream(pageName)));
      HtmlTemplate template = new HtmlTemplate(page);
      for (String each : params.keySet()) {
        template.put(each, params.get(each).toString());
      }
      writeResponse(response, template.evaluate());
    } catch (IOException e) {
      throw new IllegalStateException("Could not write response to the outputstream of servlet", e);
    }
  }

  private void writeResponse(HttpServletResponse response, String htmlContent) throws IOException {
    response.setContentType("text/html");
    response.setStatus(HttpURLConnection.HTTP_OK);
    PrintWriter writer = response.getWriter();
    writer.print(htmlContent);
    writer.flush();
  }
}
