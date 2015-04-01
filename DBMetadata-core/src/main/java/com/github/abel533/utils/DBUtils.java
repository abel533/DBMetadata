package com.github.abel533.utils;

import com.github.abel533.database.DatabaseConfig;
import com.github.abel533.database.Dialect;
import com.github.abel533.database.SimpleDataSource;
import com.github.abel533.database.introspector.DatabaseIntrospector;
import com.github.abel533.database.introspector.OracleIntrospector;
import com.github.abel533.database.introspector.PGIntrospector;
import com.github.abel533.database.introspector.SqlServerIntrospector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

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

    /**
     * 获取默认的Config
     *
     * @return
     * @throws SQLException
     */
    public DatabaseConfig getDefaultConfig() throws SQLException {
        DatabaseIntrospector introspector = getDatabaseIntrospector();
        List<String> catalogs = introspector.getCatalogs();
        List<String> schemas = introspector.getSchemas();
        DatabaseConfig config = null;
        if (catalogs.size() == 1) {
            if (schemas.size() == 1) {
                config = new DatabaseConfig(catalogs.get(0), schemas.get(0));
            } else if (schemas.size() == 0) {
                config = new DatabaseConfig(catalogs.get(0), null);
            }
        } else if (catalogs.size() == 0) {
            if (schemas.size() == 1) {
                config = new DatabaseConfig(null, schemas.get(0));
            } else if (schemas.size() == 0) {
                config = new DatabaseConfig(null, null);
            }
        }
        if (config == null) {
            switch (getDialect()) {
                case DB2:
                case ORACLE:
                    config = new DatabaseConfig(null, dataSource.getUser());
                    break;
                case MYSQL:
                    if (schemas.size() > 0) {
                        break;
                    }
                    String url = dataSource.getUrl();
                    if (url.indexOf('/') > 0) {
                        String dbName = url.substring(url.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                config = new DatabaseConfig(catalog, null);
                                break;
                            }
                        }
                    }
                    break;
                case SQLSERVER:
                    String sqlserverUrl = dataSource.getUrl();
                    String sqlserverCatalog = null;
                    if (sqlserverUrl.indexOf('/') > 0) {
                        String dbName = sqlserverUrl.substring(sqlserverUrl.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                sqlserverCatalog = catalog;
                                break;
                            }
                        }
                        if (sqlserverCatalog != null) {
                            for (String schema : schemas) {
                                if ("dbo".equalsIgnoreCase(schema)) {
                                    config = new DatabaseConfig(sqlserverCatalog, "dbo");
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case POSTGRESQL:
                    String postgreUrl = dataSource.getUrl();
                    String postgreCatalog = null;
                    if (postgreUrl.indexOf('/') > 0) {
                        String dbName = postgreUrl.substring(postgreUrl.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                postgreCatalog = catalog;
                                break;
                            }
                        }
                        if (postgreCatalog != null) {
                            for (String schema : schemas) {
                                if ("public".equalsIgnoreCase(schema)) {
                                    config = new DatabaseConfig(postgreCatalog, "public");
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    config = new DatabaseConfig();
                    break;
            }

        }
        return config;
    }
}
