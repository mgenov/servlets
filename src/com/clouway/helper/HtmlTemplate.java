package com.clouway.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HtmlTemplate {
  private final String html;
  private final Map<String, String> placeHolders = new ConcurrentHashMap<>();

  public HtmlTemplate(String html) {
    this.html = html;
  }

  public void put(String placeHolder, String value) {
    placeHolders.put(placeHolder, value);
  }

  public String evaluate() {
    String result = html;
    for (String placeHolder : placeHolders.keySet()) {
      result = result.replace("{" + placeHolder + "}", placeHolders.get(placeHolder));
    }
    return result;
  }
}
