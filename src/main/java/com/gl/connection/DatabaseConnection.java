package com.gl.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;

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
        try {
            connection = SQLConnection.getConnection();
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

