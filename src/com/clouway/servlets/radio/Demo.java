package com.clouway.servlets.radio;


import com.clouway.servlets.radio.adapter.HtmlTemplate;
import com.clouway.servlets.radio.server.JettyServer;
import com.clouway.servlets.radio.servlets.PagesServlet;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class Demo {
  public static void main(String[] args) throws IOException {
    Map<String, String> pages = new LinkedHashMap<String, String>() {{
      put("index", Files.toString(new File("src/com/clouway/servlets/radio/resources/index.html"), Charsets.UTF_8));
      put("page1", Files.toString(new File("src/com/clouway/servlets/radio/resources/page1.html"), Charsets.UTF_8));
      put("page2", Files.toString(new File("src/com/clouway/servlets/radio/resources/page2.html"), Charsets.UTF_8));
      put("page3", Files.toString(new File("src/com/clouway/servlets/radio/resources/page3.html"), Charsets.UTF_8));
    }};

    HtmlTemplate template = new HtmlTemplate();

    Map<String, HttpServlet> servlets = new LinkedHashMap<String, HttpServlet>() {{
      put("/", new PagesServlet(template, pages));
    }};

    JettyServer server = new JettyServer(8080, servlets);
    server.start();
  }
}