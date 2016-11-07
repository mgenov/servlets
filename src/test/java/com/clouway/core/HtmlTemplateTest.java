package com.clouway.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class HtmlTemplateTest {

  @Test
  public void happyPath() throws Exception {
    String content = "Hello ${name}!";
    HtmlTemplate template = new HtmlTemplate(content);
    template.put("name", "World");

    String expected = "Hello World!";
    String actual = template.evaluate();

    assertThat(actual, is(expected));
  }

  @Test
  public void multipleVariables() throws Exception {
    String content = "Hello ${name}! Today is ${day}.";
    HtmlTemplate template = new HtmlTemplate(content);

    template.put("name", "World");
    template.put("day", "Sunday");

    String expected = "Hello World! Today is Sunday.";
    String actual = template.evaluate();

    assertThat(actual, is(expected));
  }
}