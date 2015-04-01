package com.github.abel533.database;

import java.util.List;

/**
 * 内省数据库时的处理接口
 */
public abstract class AbstractDatabaseProcess implements DatabaseProcess {
    @Override
    public void processStart() {

    }

    @Override
    public void processColumn(IntrospectedTable table, IntrospectedColumn column) {

    }

    @Override
    public void processTable(IntrospectedTable table) {

    }

    @Override
    public void processComplete(List<IntrospectedTable> introspectedTables) {

    }
}
