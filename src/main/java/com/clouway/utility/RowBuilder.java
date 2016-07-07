package com.clouway.utility;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface RowBuilder {
  RowBuilder cell(Cell cell);

  String build();
}
