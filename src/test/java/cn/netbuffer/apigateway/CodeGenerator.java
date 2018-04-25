package cn.netbuffer.apigateway;

import cn.netbuffer.apigateway.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CodeGenerator {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/u?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    @Before
    public void getConnection() throws Exception {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Test
    public void testGetDatabaseMetaData() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet catalogs = databaseMetaData.getCatalogs();
        List databases = new ArrayList();
        while (catalogs.next()) {
            databases.add(catalogs.getString(1));
        }
        log.info("databases:{}", databases);
        ResultSet rs = databaseMetaData.getTables(null, null, null, null);
        List tables = new ArrayList();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        log.info("tables:{}", tables);
    }

    @Test
    public void testGetTableColumns() {
        getTable("user", connection);
    }

    private void getTable(String tableName, Connection connection) {
        String sql = "select * from " + tableName + " limit 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columeCount = meta.getColumnCount();
            log.info("表 " + tableName + "共有 " + columeCount + " 个字段");
            for (int i = 1, len = columeCount + 1; i < len; i++) {
                log.info("column:{}-{}", meta.getColumnName(i), meta.getColumnTypeName(i));
            }
        } catch (SQLException e) {
            log.error("{}", e.getMessage());
        }
    }

    /**
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#textual-template-modes
     */
    @Test
    public void testThymeleaf() {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setTemplateMode("TEXT");
        templateEngine.setTemplateResolver(stringTemplateResolver);
        Context context = new Context();
        context.setVariable("name", "World");
        context.setVariable("user", new User(1L, "admin"));
        context.setVariable("items", new String[]{"a", "b", "c"});
        StringWriter stringWriter = new StringWriter();
        templateEngine.process("hello:[(${name})]" +
                "[# th:if=\"${user.name}\"]\n" +
                "   alert('Welcome [(${user.name})]');\n" +
                "  [/]" +
                "[# th:each=\"item : ${items}\"]\n" +
                "   [(${item})]\n" +
                "[/]", context, stringWriter);
        log.info("{}", stringWriter);
    }
}