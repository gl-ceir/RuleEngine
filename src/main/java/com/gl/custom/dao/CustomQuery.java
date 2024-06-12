package com.gl.custom.dao;

import com.gl.custom.model.Result;
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
        String query = "select  * from app.gdce_data where imei  = SUBSTRING('"+imei+"' , 1, 14)  ";
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


    public static void saveInGdceApiCallHistory(Connection conn, String imei, String remark, String compliantStatus, String status, String source) {
        String query = "insert into app.gdce_api_call_history (imei,status,remark,compliant_status,source) " +
                "values( '" + imei + "', '" + status + "', '" + remark + "', '" + compliantStatus + "','" + source + "')";
        logger.debug("Query " + query);
        try (Statement st = conn.createStatement()) {
            logger.info("insert into gdce_api_call_history {} ",st.executeUpdate(query));
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
    }

    public static void saveInGdceData(Connection conn, String imei, Result r, String source, String reqId) {
        String query = "insert into app.gdce_data(imei,serial_number,registration_date,date_of_actual_import,is_custom_tax_paid, actual_imei, source,request_id ) " +
                "values( '" + imei.substring(0, 14) + "', '" + r.getSerial_number() + "', '" + r.getDate_of_registration() + "', '" + r.getDate_of_actual_import() + "','" + (r.getCustoms_duty_tax().equalsIgnoreCase("paid") ? 1 : 0) + "' , '" + imei + "', '" + source + "', '" + reqId + "'   )";
        logger.debug("Query " + query);
         try (Statement st = conn.createStatement()) {
             logger.info("insert into gdce_data {} ",st.executeUpdate(query));
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
    }
}
