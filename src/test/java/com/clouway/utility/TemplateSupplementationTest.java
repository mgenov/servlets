package com.clouway.utility;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class TemplateSupplementationTest {

  @Test
  public void oneVariable() {
    BracketsTemplate bracketsTemplate = new BracketsTemplate();
    bracketsTemplate.setTemplate("Hello, ${name} !");

    bracketsTemplate.put("name", "World");

    assertThat(bracketsTemplate.evaluate(), is(equalTo("Hello, World !")));
  }

  @Test
  public void manyVariables() {
    BracketsTemplate bracketsTemplate = new BracketsTemplate();
    bracketsTemplate.setTemplate("Congrats ${name}, you won ${amount}!");

    bracketsTemplate.put("name", "John");
    bracketsTemplate.put("amount", "a million dollars");

    assertThat(bracketsTemplate.evaluate(), is(equalTo("Congrats John, you won a million dollars!")));
  }

}