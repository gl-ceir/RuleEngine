/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine.rules;

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
public class TEST_IMEI {

    static final Logger logger = LogManager.getLogger(IMEI_NULL.class);

    static String executeRule(String[] args, Connection conn) {
        String value = ""; //  001 / 0044
        String res = "No";
        String query = "select value from sys_param where tag= 'TEST_IMEI_SERIES' ";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                value = rs.getString("value");
            }
            logger.info("Test imei vals from db " + value);
            String[] arr = value.split(",");
            for (String testImeiSeries : arr) {
                if (args[3].startsWith(testImeiSeries)) {
                    res = "Yes";
                }
            }
            logger.info("Is imei : " + args[3] + " A Test imei : " + res);
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
                    try (Statement stmt = conn.createStatement();) {

                        String qur = " insert into test_imei_details  (imei ,IMSI,  msisdn , record_type , system_type , source,raw_cdr_file_name,imei_arrival_time ,operator, file_name , created_on , modified_on    )  values "
                                + "('" + args[3] + "' , '" + args[14] + "', '" + args[12] + "' ,'" + args[15] + "' , '" + args[16] + "',  '" + args[17] + "', '" + args[18] + "', '" + args[19] + "', '" + args[20] + "',   '" + args[21] + "', now(),  now()   ) ";
                        logger.info(".test_imei_details ::Report ." + qur);
                        stmt.executeUpdate(qur);
                    } catch (Exception e) {
                        logger.debug("Error " + e);
                    }
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
