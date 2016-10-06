package com.clouway.servlets.radio.Core;

import com.clouway.servlets.radio.adapter.HtmlTemplate;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class TemplateTest {

  @Test
  public void happyPath() throws Exception {
    HtmlTemplate template = new HtmlTemplate();
    String content = "Hello ${name}!";

    template.setTemplateValue(content);
    template.put("name", "World");

    String expected = "Hello World!";
    String actual = template.evaluate();

    assertThat(actual, is(expected));
  }

  @Test
  public void multipleVariables() throws Exception {
    HtmlTemplate template = new HtmlTemplate();
    String content = "Hello ${name}! Today is ${day}.";

    template.setTemplateValue(content);
    template.put("name", "World");
    template.put("day", "Sunday");

    String expected = "Hello World! Today is Sunday.";
    String actual = template.evaluate();

    assertThat(actual, is(expected));
  }

}