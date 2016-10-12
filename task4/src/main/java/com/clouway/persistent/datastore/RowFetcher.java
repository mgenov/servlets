package com.clouway.persistent.datastore;

import java.sql.ResultSet;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public interface RowFetcher<T> {
    T fetchRow(ResultSet resultSet);
}
