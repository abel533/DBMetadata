#数据库元数据

本工具仅仅用于数据库表和字段的查询。

本工具有两种用途：

- 一种是直接使用，在Swing界面中对表和字段进行查询。

- 第二种是将本工具作为一个获取数据库元数据的基本jar包。

第一种用法很简单，不需要专门讲，所以这里先说第二种用途。

##获取数据库元数据

数据库元数据是本项目的核心，获取元数据部分的代码参考了MyBatis Generator。

想要使用本工具获取数据库元数据信息，只需要导入<b>dbmetadata-core.xxx.jar</b>即可。

###稍后会上传到maven中，并且提供jar包下载的地址。

##如何使用core

###一、引入jar包或者maven依赖

1. 在maven中加入dbmetadata-core的依赖，或者引入dbmetadata-core.xxx.jar包。

2. 你的项目中，还需要有访问数据库的jdbc驱动。

注:<b>如果你使用的sqlserver，你需要使用jtds！</b>

###二、使用方法

```java
SimpleDataSource dataSource = new SimpleDataSource(
                Dialect.MYSQL,
                "jdbc:mysql://localhost:3306/mydb",
                "root",
                ""
);
DBUtils dbUtils = null;
try {
    dbUtils = new DBUtils(dataSource);
    DatabaseIntrospector introspector = dbUtils.getDatabaseIntrospector();

    DatabaseConfig config = new DatabaseConfig("mydb", null);

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
```

在你通过`introspector.introspectTables(config)`获取到所有表的信息后，你就可以做其他操作了，例如生成代码等等。

这里需要注意`DatabaseConfig`，他有下面三个构造方法：

- DatabaseConfig()
 
- DatabaseConfig(String catalog, String schemaPattern)
 
- DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern)

一般情况下我们需要设置`catalog`和`schemaPatter`，还可以设置`tableNamePattern`来限定要获取的表。

其中`schemaPatter`和`tableNamePattern`都支持sql的`%`和`_`匹配。

使用`dbUtils.getDefaultConfig()`可以根据当前数据库的类型获取一个默认的`DatabaseConfig`，建议手动创建。

##二次开发


##Swing界面

你可以从这里下载: