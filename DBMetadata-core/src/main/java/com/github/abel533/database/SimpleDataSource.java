package com.github.abel533.database;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 数据源
 */
public final class SimpleDataSource implements DataSource {
    private Dialect dialect;
    private String url;
    private String user;
    private String pwd;
    private DataSource delegate;

    public SimpleDataSource(Dialect dialect, DataSource dataSource) {
        this.dialect = dialect;
        this.delegate = dataSource;
        try {
            Class.forName(dialect.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到指定的数据库驱动:" + dialect.getDriverClass());
        }
    }

    public SimpleDataSource(Dialect dialect, String url, String user, String pwd) {
        this.dialect = dialect;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.delegate = this;
        try {
            Class.forName(dialect.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到指定的数据库驱动:" + dialect.getDriverClass());
        }
    }

    public Dialect getDialect() {
        return dialect;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (delegate instanceof SimpleDataSource) {
            if (this.dialect.equals(Dialect.MYSQL)){
                Properties props =new Properties();
                props.setProperty("user", user);
                props.setProperty("password", pwd);
                // 获取列字段注释
                props.setProperty("remarks", "true");
                // 获取表注释
                props.setProperty("useInformationSchema", "true");
                return DriverManager.getConnection(url,props);
            }
            return DriverManager.getConnection(url, user, pwd);
        } else {
            return delegate.getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (delegate instanceof SimpleDataSource) {
            return DriverManager.getConnection(url, username, password);
        } else {
            return delegate.getConnection(username, password);
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
