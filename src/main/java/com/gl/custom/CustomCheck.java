package com.gl.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomCheck {
    static final Logger logger = LogManager.getLogger(CustomCheck.class);

    public static boolean identifyCustomComplianceStatus(Connection conn, String imei, String source) {
        if (checkInGdceData(conn, imei)) {
            return true;
        } else {
            if (getValueFromSysParam(conn, source)) {
                return checkFromApi(imei);
            } else {
                return false;
            }
        }
    }

    private static boolean checkFromApi(String imei) {
        //get DataFromApi();
        //saveInGdceData();
        return false; //
    }

    private static boolean getValueFromSysParam(Connection conn, String source) {
        String query = "select  value  from app.sys_param where tag  = '" + source + "OnlineAPICheck' ";
        logger.debug("Query " + query);
        String response = "false";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                response = rs.getString("value");
            }
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
        if (response.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }


    static boolean checkInGdceData(Connection conn, String imei) {
        String query = "select  * from app.gdce_data where imei like '" + imei + "%' ";
        logger.debug("Query " + query);
        var response = false;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                response = true;
            }
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
        return response;
    }

}
