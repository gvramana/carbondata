package org.carbondata.core.util;

/**
 * @author Venkata Ramana G on 1/4/16.
 */
public class TableIdentifier {

    public String dataBaseName;
    public String tableName;

    public TableIdentifier(String dataBaseName, String tableName) {
        this.dataBaseName = dataBaseName;
        this.tableName = tableName;
    }
}
