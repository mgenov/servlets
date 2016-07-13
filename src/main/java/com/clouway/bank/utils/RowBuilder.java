package com.clouway.bank.utils;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class RowBuilder {
  private StringBuilder builder = new StringBuilder();

  public void buildRow(Object... values) {
    builder.append("  <tbody><tr>");

    for (Object object : values) {
      builder.append("<td>")
              .append(object)
              .append("</td>\n");
    }

    builder.append(" </tr>\n")
            .append(" </tbody>");
  }

  public String getRows() {
    return builder.toString();
  }
}
