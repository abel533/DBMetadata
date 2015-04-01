package com.github.abel533.utils;

import com.github.abel533.database.Dialect;
import com.github.abel533.database.SimpleDataSource;
import com.github.abel533.database.introspector.DatabaseIntrospector;
import com.github.abel533.database.introspector.OracleIntrospector;
import com.github.abel533.database.introspector.PGIntrospector;
import com.github.abel533.database.introspector.SqlServerIntrospector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库操作
 *
 * @author liuzh
 */
public class DBUtils {
    private SimpleDataSource dataSource;
    private Connection connection;
    private LetterCase letterCase;
    private Dialect dialect;

    public DBUtils(SimpleDataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("Argument dataSource can't be null!");
        }
        this.dataSource = dataSource;
        this.dialect = dataSource.getDialect();
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initLetterCase();
    }

    private void initLetterCase() {
        try {
            DatabaseMetaData databaseMetaData = getConnection().getMetaData();
            if (databaseMetaData.storesLowerCaseIdentifiers()) {
                letterCase = LetterCase.LOWER;
            } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
                letterCase = LetterCase.UPPER;
            } else {
                letterCase = LetterCase.NORMAL;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertLetterByCase(String value) {
        if (value == null) {
            return null;
        }
        switch (letterCase) {
            case UPPER:
                return value.toUpperCase();
            case LOWER:
                return value.toLowerCase();
            default:
                return value;
        }
    }

    public DatabaseMetaData getDatabaseMetaData() {
        try {
            return getConnection().getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseIntrospector getDatabaseIntrospector() {
        return getDatabaseIntrospector(false, true);
    }

    public DatabaseIntrospector getDatabaseIntrospector(boolean forceBigDecimals, boolean useCamelCase) {
        switch (dataSource.getDialect()) {
            case ORACLE:
                return new OracleIntrospector(this, forceBigDecimals, useCamelCase);
            case POSTGRESQL:
                return new PGIntrospector(this, forceBigDecimals, useCamelCase);
            case SQLSERVER:
                return new SqlServerIntrospector(this, forceBigDecimals, useCamelCase);
            case DB2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            default:
                return new DatabaseIntrospector(this, forceBigDecimals, useCamelCase);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
            }
        }
    }

    public boolean testConnection() {
        try {
            if (!getConnection().isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return false;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        try {
            connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private enum LetterCase {
        UPPER, LOWER, NORMAL
    }

    public Dialect getDialect() {
        return dialect;
    }
}
