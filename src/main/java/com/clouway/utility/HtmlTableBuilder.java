package com.clouway.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class HtmlTableBuilder implements TableBuilder {
  private final String tableClass;
  List<RowBuilder> rows = new ArrayList<>();

  public HtmlTableBuilder(String tableClass) {
    this.tableClass = tableClass;
  }

  public RowBuilder aNewRow() {
    RowBuilder rowBuilder = new HtmlRowBuilder();
    rows.add(rowBuilder);
    return rowBuilder;
  }

  public String build() {
    String openTag = "<table  class='" + tableClass + "'>\n";
    String htmlRows = getRows();
    String closeTag = "</table>\n";

    rows = new ArrayList<>();
    return openTag + htmlRows + closeTag;
  }

  private String getRows() {
    String result = "";
    for (RowBuilder rowBuilder : rows) {
      result += rowBuilder.build();
    }
    return result;
  }

  public class HtmlRowBuilder implements RowBuilder {
    List<Cell> cells = new ArrayList<Cell>();

    public RowBuilder cell(Cell cell) {
      cells.add(cell);
      return this;
    }

    @Override
    public String build() {
      String result = "";

      for (Cell cell : cells) {
        result += "<td>";
        if (cell != null) {
          result += cell.toString();
        }
        result += "</td>\n";
      }
      cells = new ArrayList<>();
      return "<tr>" + result + "</tr>\n";
    }
  }
}
