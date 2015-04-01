package com.github.abel533.component;

import com.github.abel533.database.*;
import com.github.abel533.utils.DBMetadataUtils;
import com.github.abel533.utils.StringUtils;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库表数据
 *
 * @author liuzh
 * @since 2015-03-19
 */
public class DBData {
    private static DBServer dbServer;
    private static DBMetadataUtils dbMetadataUtils;
    private static SimpleDataSource dataSource;
    //登录系统的时候操作下面两项
    public static DBServerList dbServerList;
    public static List<String> dbServers;

    //选择配置后
    public static DatabaseConfig config;
    public static List<String> catalogs;
    public static List<String> schemas;
    private static List<IntrospectedTable> tables;
    //columnMap - 方便点击表的时候显示字段
    private static Map<String, IntrospectedTable> tableMap;
    private static Map<String, List<IntrospectedColumn>> columnMap;

    public static void init(DBServer server) {
        dbServer = server;
        dataSource = new SimpleDataSource(server.getDialect(), server.getUrl(), server.getUser(), server.getRealPwd());
        dbMetadataUtils = new DBMetadataUtils(dataSource);
        try {
            initConfig();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testConnection() {
        return dbMetadataUtils.testConnection();
    }

    public static String convertLetterByCase(String value) {
        return dbMetadataUtils.convertLetterByCase(value);
    }

    /**
     * 初始化默认的Config
     *
     * @throws SQLException
     */
    private static void initConfig() throws SQLException {
        DBData.catalogs = dbMetadataUtils.getCatalogs();
        DBData.schemas = dbMetadataUtils.getSchemas();
        DBData.config = dbMetadataUtils.getDefaultConfig();
    }

    public static void initTables() throws SQLException {
        if (DBData.config != null) {
            DBData.setTables(dbMetadataUtils.introspectTables(DBData.config));
        }
    }


    public static DefaultComboBoxModel<String> getCatalogModel() {
        return new DefaultComboBoxModel<String>(catalogs.toArray(new String[]{}));
    }

    public static DefaultComboBoxModel<String> getSchemaModel() {
        return new DefaultComboBoxModel<String>(schemas.toArray(new String[]{}));
    }

    public static void setTables(List<IntrospectedTable> tables) {
        if (StringUtils.isEmpty(tables)) {
            tables = new ArrayList<IntrospectedTable>(0);
        }
        sortTables(tables);
        DBData.tables = tables;
        tableMap = new HashMap<String, IntrospectedTable>();
        columnMap = new HashMap<String, List<IntrospectedColumn>>();
        if (tables != null && tables.size() > 0) {
            for (IntrospectedTable table : tables) {
                tableMap.put(table.getName(), table);
                columnMap.put(table.getName(), table.getAllColumns());
            }
        }
    }

    private static void sortTables(List<IntrospectedTable> tables) {
        if (StringUtils.isNotEmpty(tables)) {
            Collections.sort(tables, new Comparator<IntrospectedTable>() {
                @Override
                public int compare(IntrospectedTable o1, IntrospectedTable o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
    }

    private static void sortColumns(List<IntrospectedColumn> columns) {
        if (StringUtils.isNotEmpty(columns)) {
            Collections.sort(columns, new Comparator<IntrospectedColumn>() {
                @Override
                public int compare(IntrospectedColumn o1, IntrospectedColumn o2) {
                    int result = o1.getTableName().compareTo(o2.getTableName());
                    if (result == 0) {
                        result = o1.getName().compareTo(o2.getName());
                    }
                    return result;
                }
            });
        }
    }

    public static List<IntrospectedTable> getAllTables() {
        return tables;
    }

    public static IntrospectedTable getTable(String tableName) {
        return tableMap.get(tableName);
    }

    public static List<IntrospectedColumn> getTableColumns(String tableName) {
        return columnMap.get(tableName);
    }

    /**
     * 查询表
     *
     * @param searchText
     * @param searchComment
     * @param matchType
     * @param caseSensitive
     * @return
     */
    public static List<IntrospectedTable> selectTables(
            String searchText, String searchComment, MatchType matchType, boolean caseSensitive) {
        List<IntrospectedTable> answer = new ArrayList<IntrospectedTable>();
        if (tables == null || tables.size() == 0) {
            return answer;
        }
        if (StringUtils.isEmpty(searchText) && StringUtils.isEmpty(searchComment)) {
            return tables;
        }
        for (IntrospectedTable table : tables) {
            if (table.filter(searchText, searchComment, matchType, caseSensitive)) {
                answer.add(table);
            }
        }
        sortTables(answer);
        return answer;
    }

    /**
     * 查询字段
     *
     * @param searchText
     * @param searchComment
     * @param matchType
     * @param caseSensitive
     * @return
     */
    public static List<IntrospectedColumn> selectColumns(
            String searchText, String searchComment, MatchType matchType, boolean caseSensitive) {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        if (tables == null || tables.size() == 0) {
            return answer;
        }
        if (StringUtils.isEmpty(searchText) && StringUtils.isEmpty(searchComment)) {
            return null;
        }
        for (IntrospectedTable table : tables) {
            for (IntrospectedColumn column : table.getAllColumns()) {
                if (column.filter(searchText, searchComment, matchType, caseSensitive)) {
                    answer.add(column);
                }
            }
        }
        sortColumns(answer);
        return answer;
    }
}
