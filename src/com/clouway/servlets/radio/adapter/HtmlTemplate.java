package com.clouway.servlets.radio.adapter;

import com.clouway.servlets.radio.Core.Template;

import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlTemplate implements Template {
  private final Map<String, String> placeHolderToValue = new LinkedHashMap<String, String>();
  private String templateValue;

  public void setTemplateValue(String templateValue) {
    this.templateValue = templateValue;
  }

  public void put(String placeHolder, String value) {
    placeHolderToValue.put(placeHolder, value);
  }

  public String evaluate() {
    // Missing template validation is omitted for shortening purposes.
    String evaluationResult = templateValue;
    for (String placeHolder : placeHolderToValue.keySet()) {
      evaluationResult = evaluationResult.replaceAll("\\$\\{" + placeHolder + "\\}", placeHolderToValue.get(placeHolder));
    }

    return evaluationResult;
  }
}