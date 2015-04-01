package com.github.abel533;


import com.github.abel533.database.*;
import com.github.abel533.database.introspector.DatabaseIntrospector;
import com.github.abel533.utils.DBUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * @author liuzh
 */
public class DatabaseTest {

    public static void main(String[] args) {
        SimpleDataSource dataSource = new SimpleDataSource(
                Dialect.MYSQL,
                "jdbc:mysql://192.168.123.151:3306/nmgsbx",
                "nmgsbx",
                "654321"
        );
        DBUtils dbUtils = null;
        try {
            dbUtils = new DBUtils(dataSource);
            DatabaseIntrospector introspector = dbUtils.getDatabaseIntrospector();

            printStringList(introspector.getCatalogs(), "Catalogs");

            printStringList(introspector.getSchemas(), "Schemas");

            printStringList(introspector.getTableTypes(), "TableTypes");

            DatabaseConfig config = new DatabaseConfig("nmgsbx", null);

            List<IntrospectedTable> list = introspector.introspectTables(config);

            for (IntrospectedTable table : list) {
                System.out.println("===============" + table.getName() + ":" + table.getRemarks() + "==============");
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
        } finally {
            if (dbUtils != null) {
                dbUtils.closeConnection();
            }
        }
    }

    public static void printStringList(List<String> list, String title) {
        if (list != null) {
            System.out.println("===========" + title + "===========");
            for (String string : list) {
                System.out.println(string);
            }
        }
    }

}
