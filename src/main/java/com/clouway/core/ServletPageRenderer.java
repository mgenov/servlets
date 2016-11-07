package com.clouway.core;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * ServletPageRenderer is a page renderer which is rendenring page templates to the {@link HttpServletResponse}.
 *
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public interface ServletPageRenderer {

  /**
   * Renders the provided page as content to the servlet response.
   * @param pageName the name of the page which to be rendered
   * @param params the params that need to be passed to the page template
   * @param response the servlet response to which content will be written
   * @throws IOException is thrown in case of IO error
   */
  void renderPage(String pageName, Map<String, Object> params, HttpServletResponse response) throws IOException;
}
