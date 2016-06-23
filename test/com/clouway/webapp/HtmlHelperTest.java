package com.clouway.webapp;

import com.clouway.templates.HtmlHelper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HtmlHelperTest {
  @Test
  public void happyPath() throws Exception {
    HtmlHelper template = new HtmlHelper("test/com/clouway/webapp/testFile.txt");
    String actual = template.loadResource();

    assertThat(actual, containsString("This is only for test"));
  }

  @Test
  public void loadMissingResource() throws Exception {
    HtmlHelper template = new HtmlHelper("");
    String actual = template.loadResource();

    assertThat(actual, is("Error 404! Page not found"));
  }
}
