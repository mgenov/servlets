package com.clouway.utility;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class HtmlTableCell implements Cell{
  private Object value;

  public HtmlTableCell(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
