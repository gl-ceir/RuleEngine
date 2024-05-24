package com.gl.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {
    static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    @Autowired
    static  Environment en;

    @Value("db_url")
   static String a;
    @Value("dbUsername")
    static  String b;

    private DatabaseConnection() {}
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    String URL = System.getenv("db_url");
                    String USER = System.getenv("dbUsername");
                    String PASSWORD = System.getenv("dbPassword");

                    logger.info(  ""+  en.getActiveProfiles());
                    logger.info("credentials are {} , {} , {}", URL, USER, PASSWORD);
                    logger.info("SAmple credentials  {} , {} ", a, b);

                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        }
        return connection;
    }
}


//Connection connection = DatabaseConnection.getConnection();
