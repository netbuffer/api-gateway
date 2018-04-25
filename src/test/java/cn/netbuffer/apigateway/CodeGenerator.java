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
        StringBuilder databases = new StringBuilder();
        while (catalogs.next()) {
            databases.append(catalogs.getString(1)).append(" | ");
        }
        log.info("databases:{}", databases);
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