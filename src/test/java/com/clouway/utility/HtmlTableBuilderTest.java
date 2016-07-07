package com.clouway.utility;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class HtmlTableBuilderTest {
  TableBuilder tableBuilder;

  @Before
  public void setUp() {
    tableBuilder = new HtmlTableBuilder("table table-hover table-bordered");
  }

  @Test
  public void buildOneRowTable() {
    RowBuilder rowBuilder = tableBuilder.aNewRow();
    rowBuilder.cell(new HtmlTableCell(1));
    rowBuilder.cell(new HtmlTableCell("John"));
    String table = tableBuilder.build();

    String expectedResult = "<table  class='table table-hover table-bordered'>\n" +
            "<tr><td>1</td>\n" +
            "<td>John</td>\n" +
            "</tr>\n" +
            "</table>\n";
    assertThat(table, is(equalTo(expectedResult)));
  }

  @Test
  public void buildTwoRowsTable() {
    RowBuilder rowBuilder = tableBuilder.aNewRow();
    rowBuilder.cell(new HtmlTableCell(new Date(12345))).cell(new HtmlTableCell("Jhon")).cell(new HtmlTableCell("loged in"));

    RowBuilder secondRowBuilder = tableBuilder.aNewRow();
    secondRowBuilder.cell(new HtmlTableCell("None")).cell(new HtmlTableCell("What"));

    String expectedResult = "<table  class='table table-hover table-bordered'>\n" +
            "<tr><td>Thu Jan 01 02:00:12 EET 1970</td>\n" +
            "<td>Jhon</td>\n" +
            "<td>loged in</td>\n" +
            "</tr>\n" +
            "<tr><td>None</td>\n" +
            "<td>What</td>\n" +
            "</tr>\n" +
            "</table>\n";

    assertThat(tableBuilder.build(), is(equalTo(expectedResult)));
  }

  @Test
  public void emptyCell() {
    RowBuilder rowBuilder = tableBuilder.aNewRow();
    rowBuilder.cell(new HtmlTableCell(new Date(768654))).cell(new HtmlTableCell("Jhon")).cell(new HtmlTableCell("loged in"));

    RowBuilder secondRowBuilder = tableBuilder.aNewRow();
    secondRowBuilder.cell(new HtmlTableCell("None")).cell(null).cell(new HtmlTableCell("What"));

    String expectedResult = "<table  class='table table-hover table-bordered'>\n" +
            "<tr><td>Thu Jan 01 02:12:48 EET 1970</td>\n" +
            "<td>Jhon</td>\n" +
            "<td>loged in</td>\n" +
            "</tr>\n" +
            "<tr><td>None</td>\n" +
            "<td></td>\n" +
            "<td>What</td>\n" +
            "</tr>\n" +
            "</table>\n";

    assertThat(tableBuilder.build(), is(equalTo(expectedResult)));
  }

  @Test
  public void emptyRow() {
    RowBuilder rowBuilder = tableBuilder.aNewRow();
    rowBuilder.cell(new HtmlTableCell(new Date(12345))).cell(new HtmlTableCell("Jhon")).cell(new HtmlTableCell("loged in"));

    tableBuilder.aNewRow();

    RowBuilder secondRowBuilder = tableBuilder.aNewRow();
    secondRowBuilder.cell(new HtmlTableCell("None")).cell(new HtmlTableCell("What")).cell(new HtmlTableCell("Really"));

    tableBuilder.aNewRow();
    String expectedResult = "<table  class='table table-hover table-bordered'>\n" +
            "<tr><td>Thu Jan 01 02:00:12 EET 1970</td>\n" +
            "<td>Jhon</td>\n" +
            "<td>loged in</td>\n" +
            "</tr>\n" +
            "<tr></tr>\n" +
            "<tr><td>None</td>\n" +
            "<td>What</td>\n" +
            "<td>Really</td>\n" +
            "</tr>\n" +
            "<tr></tr>\n" +
            "</table>\n";

    assertThat(tableBuilder.build(), is(equalTo(expectedResult)));
  }
}
