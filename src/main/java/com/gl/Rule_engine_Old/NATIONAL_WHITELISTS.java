/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.Rule_engine_Old;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author maverick
 */
public class NATIONAL_WHITELISTS {

    static final Logger logger = LogManager.getLogger(NATIONAL_WHITELISTS.class);

    static String executeRule(String[] args, Connection conn) {
        String res = "No";
        String query = "select count(*) from national_whitelist where imei like '" + args[3] + "%' ";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                res = "Yes";
            }
            logger.info("value from db " + res);
        } catch (Exception e) {
            logger.error("Error.." + e);
        }
        return res;
    }

    static String executeAction(String[] args, Connection conn, BufferedWriter bw) {
        try {
            logger.debug("Action::: " + args[13]);
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
                    String fileString = args[15] + " ,Error Code :CON_RULE_0019, Error Description : IMEI/ESN/MEID is already present in the system  ";
                    bw.write(fileString);
                    bw.newLine();

                }
                break;
                case "Report": {
                    logger.debug("Action is Report");

                }
                break;
                case "Report2": {
                    logger.debug("Action is Report");
                }
                break;
                case "SYS_REG": {
                    logger.debug("Action is SYS_REG");
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
