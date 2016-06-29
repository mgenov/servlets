package com.clouway.utility;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Reading the contents of a file
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class FileReader implements Reader {

  /**
   * reads the file from the file path
   *
   * @param source the path to the file
   * @return
   */
  @Override
  public String read(String source) {
    File file = new File(source);
    URL url = null;
    try {
      url = file.toURI().toURL();
      InputStream in = url.openStream();

      return IOUtils.toString(in);

    } catch (IOException e) {
      return "404 File not found!";
    }
  }
}
