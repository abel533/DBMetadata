package com.github.abel533.service;

import com.github.abel533.component.DBData;
import com.github.abel533.database.IntrospectedColumn;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.database.MatchType;
import com.github.abel533.utils.I18n;
import com.github.abel533.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ToolsService {

    private List<IntrospectedTable> EMPTY = new ArrayList<IntrospectedTable>(0);

    private Object[][] EMPTY_DATA = new Object[][]{};

    private String[] tableNames = I18n.key("dbTableModel.columns").split(",");
    private DefaultTableModel dbTableModel = new DefaultTableModel(EMPTY_DATA, tableNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private String[] fieldNames = I18n.key("dbFieldModel.columns").split(",");
    private DefaultTableModel dbFieldModel = new DefaultTableModel(EMPTY_DATA, fieldNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            //tablename,name,type(length),remarks,pk,nullable,defaultValue
            switch (columnIndex) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return String.class;
                case 4:
                case 5:
                    return Boolean.class;
                default:
                    return Object.class;
            }
        }
    };

    public DefaultTableModel getDbTableModel() {
        return dbTableModel;
    }

    public DefaultTableModel getDbFieldModel() {
        return dbFieldModel;
    }

    /**
     * 处理标题列的居中
     *
     * @param dbField
     */
    public void prcessDbFieldHeader(JTable dbField) {
        dbField.getColumn(fieldNames[4]).setPreferredWidth(40);
        dbField.getColumn(fieldNames[4]).setMinWidth(40);
        dbField.getColumn(fieldNames[4]).setMaxWidth(40);
        dbField.getColumn(fieldNames[5]).setPreferredWidth(40);
        dbField.getColumn(fieldNames[5]).setMinWidth(40);
        dbField.getColumn(fieldNames[5]).setMaxWidth(40);
    }

    public synchronized void loadTableFields(final String tableName) {
        loadColumns(DBData.getTableColumns(tableName), true);
    }

    public void loadTableColumnsByRowIndex(int row) {
        String tableName = dbTableModel.getValueAt(row, 0).toString();
        loadTableFields(tableName);
    }

    public String getChooseTableTextByRowIndex(int row) {
        StringBuilder chooseBuilder = new StringBuilder();
        for (int i = 0; i < dbTableModel.getColumnCount(); i++) {
            chooseBuilder.append(StringUtils.getNotNull(dbTableModel.getValueAt(row, i))).append(" - ");
        }
        return chooseBuilder.substring(0, chooseBuilder.length() - 3);
    }

    public String getChooseFieldTextByRowIndex(int row) {
        StringBuilder chooseBuilder = new StringBuilder();
        for (int i = 0; i < dbFieldModel.getColumnCount(); i++) {
            chooseBuilder.append(StringUtils.getNotNull(dbFieldModel.getValueAt(row, i))).append(" - ");
        }
        return chooseBuilder.substring(0, chooseBuilder.length() - 3);
    }

    public String getTableNameByRowIndex(int row) {
        return dbTableModel.getValueAt(row, 0).toString();
    }

    public String getFieldNameByRowIndex(int row) {
        return dbFieldModel.getValueAt(row, 1).toString();
    }

    public synchronized void search(String searchText, String searchComment, MatchType matchType, boolean table, boolean caseSensitive) {
        if (table) {
            loadTables(DBData.selectTables(searchText, searchComment, matchType, caseSensitive), 0);
        } else {
            List<IntrospectedColumn> columns = DBData.selectColumns(searchText, searchComment, matchType, caseSensitive);
            if (columns == null) {
                loadTables(DBData.getAllTables(), 0);
            } else {
                List<IntrospectedTable> tables = new ArrayList<IntrospectedTable>();
                Map<String, Integer> tableMap = new HashMap<String, Integer>();
                for (IntrospectedColumn column : columns) {
                    if (tableMap.containsKey(column.getTableName())) {
                        tableMap.put(column.getTableName(), tableMap.get(column.getTableName()) + 1);
                    } else {
                        tables.add(DBData.getTable(column.getTableName()));
                        tableMap.put(column.getTableName(), 1);
                    }
                }
                loadTables(tables, -1);
                loadColumns(columns, false);
            }
        }
    }

    public synchronized void loadAll() {
        loadTables(DBData.getAllTables(), 0);
    }

    public synchronized void clearAll() {
        loadTables(EMPTY, -1);
    }

    public synchronized void loadTables(final List<IntrospectedTable> tables, final int selectIndex) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                while (dbTableModel.getRowCount() > 0) {
                    dbTableModel.removeRow(0);
                }
                for (IntrospectedTable table : tables) {
                    dbTableModel.addRow(new Object[]{
                            table.getName(),
                            table.getRemarks()
                    });
                }
                if (tables.size() == 0) {
                    while (dbFieldModel.getRowCount() > 0) {
                        dbFieldModel.removeRow(0);
                    }
                } else if (tables.size() > 0 && selectIndex > -1) {
                    loadTableFields(tables.get(selectIndex).getName());
                }
            }
        });
    }

    public synchronized void loadColumns(final List<IntrospectedColumn> columns, final boolean tableName) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                while (dbFieldModel.getRowCount() > 0) {
                    dbFieldModel.removeRow(0);
                }
                for (int i = 0; i < columns.size(); i++) {
                    //tablename,name,type(length),remarks,pk,nullable,defaultValue
                    String tName = columns.get(i).getTableName();
                    if (tableName && i > 0) {
                        tName = "";
                    }
                    StringBuilder typeBuilder = new StringBuilder(columns.get(i).getType());
                    if (!columns.get(i).isJDBCDateColumn() && !columns.get(i).isJDBCTimeColumn()) {
                        typeBuilder.append("(");
                        typeBuilder.append(columns.get(i).getLength());
                        if (columns.get(i).getScale() > 0) {
                            typeBuilder.append(",").append(columns.get(i).getScale());
                        }
                        typeBuilder.append(")");
                    }
                    dbFieldModel.addRow(new Object[]{
                            tName,
                            columns.get(i).getName(),
                            typeBuilder.toString(),
                            columns.get(i).getRemarks(),
                            columns.get(i).isPk(),
                            columns.get(i).isNullable(),
                            columns.get(i).getDefaultValue()
                    });
                }
            }
        });
    }


}
