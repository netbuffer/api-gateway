package cn.netbuffer.apigateway;

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

    @Test
    public void testCreate() {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setTemplateMode("TEXT");
        templateEngine.setTemplateResolver(stringTemplateResolver);
        Context context = new Context();
        context.setVariable("name", "World");
        StringWriter stringWriter = new StringWriter();
        templateEngine.process("hello  <h3 th:text=\"${name}\">this is a greeting</h3>", context, stringWriter);
        log.info("text:{}", stringWriter.toString());
    }
}
