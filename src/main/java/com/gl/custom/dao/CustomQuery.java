package com.gl.custom.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomQuery {
    static final Logger logger = LogManager.getLogger(CustomQuery.class);


    public static boolean getSourceValueFromSysParam(Connection conn, String source) {
        var response = getValueFromSysParam(conn, source + "OnlineAPICheck");
        if (response.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getValueFromSysParam(Connection conn, String tag) {
        String query = "select  value  from app.sys_param where tag  = '" + tag + "' ";
        logger.debug("Query " + query);
        String response = "";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                response = rs.getString("value");
            }
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
        return response;
    }


    public static boolean checkInGdceData(Connection conn, String imei) {
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


    public static void saveInGdceApiCallHistory(Connection conn, String imei, Object response, String compliantStatus, String status) {
        String query = "insert into app.gdce_api_call_history() ";
        logger.debug("Query " + query);
        try (Statement st = conn.createStatement()) {
            logger.info("insert into gdce_api_call_history {} ",st.executeUpdate(query));
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }

    }

    public static void saveInGdceData(Connection conn, String imei, Object response) {
        String query = "insert into app.gdce_data ( )  ";
        logger.debug("Query " + query);
         try (Statement st = conn.createStatement()) {
             logger.info("insert into gdce_data {} ",st.executeUpdate(query));
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
    }
}
