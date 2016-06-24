package com.clouway.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HtmlHelper {
  private final String path;

  public HtmlHelper(String path) {
    this.path = path;
  }

  public String loadResource() {
    StringBuilder builder = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      return builder.toString();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return "Error 404! Page not found!";
  }
}
