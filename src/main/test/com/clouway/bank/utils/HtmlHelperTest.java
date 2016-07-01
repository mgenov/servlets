package com.clouway.bank.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HtmlHelperTest {
  @Test
  public void happyPath() throws Exception {
    HtmlHelper helper = new HtmlHelper("test/utils/testFile.txt");
    String actual = helper.loadResource();

    assertThat(actual, containsString("This is test file"));
  }

  @Test
  public void loadMissingResource() throws Exception {
    HtmlHelper helper = new HtmlHelper("");
    String actual = helper.loadResource();

    assertThat(actual, containsString("Error 404! Page not found"));
  }
}
