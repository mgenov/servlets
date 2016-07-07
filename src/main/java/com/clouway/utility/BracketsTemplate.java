package com.clouway.utility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gets string and replaces the given place holders in ${} brackets
 * with associated value, and gives the new string back
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class BracketsTemplate implements Template {
  private final Map<String, String> placeHolderToValue = new LinkedHashMap<String, String>();
  private String template;
  private Reader reader;

  public BracketsTemplate(Reader reader) {
    this.reader = reader;
  }

  /**
   * setting the original string template value
   *
   * @param template the original string value
   */
  public void setTemplate(String template) {
    this.template = template;
  }

  /**
   * Uses Reader to get the template
   *
   * @param url
   */
  @Override
  public void loadFromFile(String url) {
    setTemplate(reader.read(url));
  }

  /**
   * associates place holders with values to be substituted
   *
   * @param placeHolder place holder to be substituted
   * @param value       the value to be set
   */
  public void put(String placeHolder, String value) {
    placeHolderToValue.put(placeHolder, value);
  }


  /**
   * substitutes the placeholders for the values
   * and returns the new string
   *
   * @return
   */
  public String evaluate() {
    String evaluationResult = template;
    for (String placeHolder : placeHolderToValue.keySet()) {
      evaluationResult = evaluationResult.replaceAll("\\$\\{" + placeHolder + "\\}", placeHolderToValue.get(placeHolder));
    }

    return evaluationResult;
  }
}
