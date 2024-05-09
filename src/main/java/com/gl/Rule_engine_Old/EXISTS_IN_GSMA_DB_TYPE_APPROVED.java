package com.gl.Rule_engine_Old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EXISTS_IN_GSMA_DB_TYPE_APPROVED {

    static final Logger logger = LogManager.getLogger(EXISTS_IN_GSMA_DB_TYPE_APPROVED.class);

    static String executeRule(String[] args, Connection conn) {
        if (args[3].length() < 8) {
            logger.info("length less than 8 ->" + args[3].length());
            return "No";
        }
        Statement stmt = null;
        ResultSet result = null;
        String res = "No";
        try {
            logger.info("[Starting]");
            stmt = conn.createStatement();
                String query = "select count(device_id) from app.mobile_device_repository  where device_id='" + args[3].substring(0, 8) + "' and is_type_approved=1";
            logger.info("[" + query +"]");
            result = stmt.executeQuery(query);
            try {
                while (result.next()) {
                    res = result.getString(1);
                }
            } catch (Exception e) {
                logger.error("");
            }
            if (!res.equals("0")) {
                res = "Yes";
            } else {
                res = "No";
            }
            result.close();
            stmt.close();
        } catch (Exception e) {
            logger.error("error.." + e);
        } finally {
            try {
                result.close();
                stmt.close();
            } catch (Exception ex) {
                logger.error("Error for Finally " + ex);
            }
        }
        return res;
    }

    static String executeAction(String[] args, Connection conn, BufferedWriter bw) {
        try {
            switch (args[13]) {
                case "Allow": {
                    logger.debug("Action is Allow");
                }
                break;
                case "Skip": {
                    logger.debug("Action is Skip");
                }
                break;
                case "Reject": {
                    logger.debug("Action is Reject");
                    String fileString = args[15] + " , Error Code :CON_RULE_0003 , Error Description :TAC in IMEI is not approved TAC from GSMA Tac Details ";
                    bw.write(fileString);
                    bw.newLine();
                }
                break;
                case "Block": {
                    logger.debug("Action is Block");
                }
                break;
                case "Report": {
                    logger.debug("Action is Report");
                }
                break;
                case "SYS_REG": {
                    logger.debug("Action is SYS_REG");
                }
                break;
                case "NAN": {
                    logger.debug("Action is NAN");
                    String fileString = args[15] + " , Error Code :CON_RULE_0002, Error Description :Could not connect to GSMA server.Try after Some Time.  ";
                    bw.write(fileString);
                    bw.newLine();
                }
                break;
                case "USER_REG": {
                    logger.debug("Action is USER_REG");
                }
                break;
                default:
                    logger.debug(" The Action " + args[13] + "  is Not Defined  ");
            }

            return "Success";
        } catch (Exception e) {
            logger.debug(" Error " + e);
            return "Failure";
        }
    }

}
