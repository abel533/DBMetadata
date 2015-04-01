package com.github.abel533;


import com.github.abel533.database.Dialect;
import com.github.abel533.database.IntrospectedColumn;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.database.SimpleDataSource;
import com.github.abel533.utils.DBMetadataUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * @author liuzh
 */
public class DatabaseTest {

    public static void main(String[] args) {
        SimpleDataSource dataSource = new SimpleDataSource(
                Dialect.MYSQL,
                "jdbc:mysql://localhost:3306/test",
                "root",
                ""
        );
        DBMetadataUtils dbMetadataUtils = null;
        try {
            dbMetadataUtils = new DBMetadataUtils(dataSource);

            List<IntrospectedTable> list = dbMetadataUtils.introspectTables(dbMetadataUtils.getDefaultConfig());

            for (IntrospectedTable table : list) {
                System.out.println(table.getName() + ":");
                for (IntrospectedColumn column : table.getAllColumns()) {
                    System.out.println(column.getName() + " - " +
                            column.getJdbcTypeName() + " - " +
                            column.getJavaProperty() + " - " +
                            column.getJavaProperty() + " - " +
                            column.getFullyQualifiedJavaType().getFullyQualifiedName() + " - " +
                            column.getRemarks());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
