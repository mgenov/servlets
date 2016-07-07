package com.clouway.utility;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface TableBuilder {

  RowBuilder aNewRow();

  String build();
}
