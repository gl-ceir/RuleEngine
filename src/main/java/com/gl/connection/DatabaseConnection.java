package com.gl.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class DatabaseConnection {

    static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    private DatabaseConnection() {
    }
    private static Connection connection = null;

    @PostConstruct
    public static void initialize() throws Exception {
        createConnection();
    }

    private static void createConnection() {
        try {//   var propertyReader = new PropertyReader();
            String jdbcDriver = "com.mysql.cj.jdbc.Driver"; //  propertyReader.getConfigPropValue("jdbc_driver").trim();
            String dbURL = "jdbc:mysql://64.227.146.191/app"; //   propertyReader.getConfigPropValue("db_url").trim();
            String username = "cdrp"; //  propertyReader.getConfigPropValue("dbUsername").trim();
            String password = "Cdrp@1234";
//            if (jdbcDriver.contains("mysql")) password = propertyReader.getConfigPropValue("dbPassword").trim();
//            else password = decryptor(propertyReader.getConfigPropValue("dbEncyptPassword").trim());
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (Exception e) {
            logger.error("Not able to conn {}", e.getLocalizedMessage());
        }
    }

    public static String decryptor(String encryptedText) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));
        return encryptor.decrypt(encryptedText);
    }


    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                createConnection();
        } catch (Exception e) {
            logger.error("{}", e.getLocalizedMessage());
        }
        logger.info("Connection {}", connection);
        return connection;
    }
}

