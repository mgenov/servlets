package com.clouway.templates;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class HtmlTemplateTest {
  @Test
  public void happyPath() throws Exception {
    HtmlTemplate template = new HtmlTemplate("This is {name}");
    template.put("name", "Lilia");
    String actual = template.evaluate();

    assertThat(actual, is(equalTo("This is Lilia")));
  }

  @Test
  public void replaceMultipleValues() {
    HtmlTemplate template = new HtmlTemplate("This are {name} and {anotherName}");
    template.put("name", "Lilia");
    template.put("anotherName", "Maria");
    String actual = template.evaluate();

    assertThat(actual, is(equalTo("This are Lilia and Maria")));
  }
}
