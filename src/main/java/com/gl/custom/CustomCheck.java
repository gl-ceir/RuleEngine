package com.gl.custom;

import com.gl.connection.DatabaseConnection;
import com.gl.custom.model.CustomApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;

import static com.gl.custom.dao.CustomQuery.*;
import static com.gl.custom.service.HttpApiConnection.getDataFromApi;

@Component
public class CustomCheck {
    static final Logger logger = LogManager.getLogger(CustomCheck.class);

    public static String identifyCustomComplianceStatus(String imei, String source) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            logger.info("Checking Connection : {}", conn);
        } catch (Exception e) {
        }
       return identifyCustomComplianceStatus( conn,  imei,  source, "");
    }

    public static String identifyCustomComplianceStatus(Connection conn, String imei, String source) {
        return  identifyCustomComplianceStatus( conn,  imei,  source, "");
    }

    public static String identifyCustomComplianceStatus(Connection conn, String imei, String source,String reqId) {
        var result = checkInGdceData(conn, imei);
        if (!result.equalsIgnoreCase("false")) {
            return result;
        } else {
            if (getSourceValueFromSysParam(conn, source)) {
                return checkFromApi(conn, imei, source, reqId);
            } else {
                return "false";
            }
        }
    }

    private static String checkFromApi(Connection conn, String imei, String source, String reqId) {
            CustomApiResponse r = getDataFromApi(conn,imei);
        logger.info("fin Api response {}", r);
        if (r.getStatus().equalsIgnoreCase("Error")) {
            saveInGdceApiCallHistory(conn, imei, r.getMessage(), "", "Error", source);
            return "ERROR"; // return blank
        } else if (r.getStatus().equalsIgnoreCase("false")) {
            saveInGdceApiCallHistory(conn, imei, r.getMessage(), "false", "Success", source);
            return "FALSE";
        } else {
            saveInGdceData(conn, imei, r.getResult() ,source ,reqId);  //
            saveInGdceApiCallHistory(conn, imei, r.getMessage(), "true", "Success", source);
            return "TRUE";
            }
    }
}
//       try{
//           String URL = System.getenv("db_url");
//           String USER = System.getenv("dbUsername");
//           String PASSWORD = System.getenv("dbPassword");
//           logger.info("credentials are {} , {} , {}", URL, USER, PASSWORD);
//           logger.info("SAmple credentials  {} , {} ", a, b);
//           create internally conn ->  or crete con on call
//           Connection connection = DatabaseConnection.getConnection();
//           logger.info(" Connection:" + connection) ;
//       }catch (Exception e){
//           logger.error("Error in Conn:" + e) ;
//       }
