package com.clouway.utility;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class TemplateSupplementationTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  Reader reader;

  @Test
  public void oneVariable() {
    BracketsTemplate bracketsTemplate = new BracketsTemplate(reader);
    bracketsTemplate.setTemplate("Hello, ${name} !");

    bracketsTemplate.put("name", "World");

    assertThat(bracketsTemplate.evaluate(), is(equalTo("Hello, World !")));
  }

  @Test
  public void manyVariables() {
    BracketsTemplate bracketsTemplate = new BracketsTemplate(reader);
    bracketsTemplate.setTemplate("Congrats ${name}, you won ${amount}!");


    bracketsTemplate.put("name", "John");
    bracketsTemplate.put("amount", "a million dollars");

    assertThat(bracketsTemplate.evaluate(), is(equalTo("Congrats John, you won a million dollars!")));
  }

  @Test
  public void templateFromFile() {
    String file = "someFile.txt";
    BracketsTemplate bracketsTemplate = new BracketsTemplate(reader);
    context.checking(new Expectations() {{
      oneOf(reader).read(file);
      will(returnValue("${name}, is slacking"));
    }});
    bracketsTemplate.loadFromFile(file);
    bracketsTemplate.put("name", "Ivaylo");

    assertThat(bracketsTemplate.evaluate(), is(equalTo("Ivaylo, is slacking")));
  }

}