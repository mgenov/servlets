package com.clouway.servlets.radio.Core;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public interface Template {

  void setTemplateValue(String templateValue);

  void put(String placeHolder, String value);

  String evaluate();
}