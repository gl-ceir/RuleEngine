/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine_Old;

import java.sql.Connection;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
class SAME_SERIAL_UNBLOCK {

    static final Logger logger = LogManager.getLogger(SAME_SERIAL_UNBLOCK.class);

    static String executeRule(String[] args, Connection conn) {
        String res = "";
        Statement stmt = null;
        ResultSet result = null;
        try {
            String opr1 = null;
            String qury = "  select SN_OF_DEVICE    from DEVICE_OPERATOR   where imei_esn_meid = '" + args[3] + "'  ";
            logger.debug("qury " + qury);
            stmt = conn.createStatement();
            result = stmt.executeQuery(qury);
            try {
                while (result.next()) {
                    opr1 = result.getString(1);
                }
            } catch (Exception e) {
                logger.error(" sql excp " + e);
            }
//            logger.info("   111");
//            if (args[4] == null) {
//                logger.info(" !! args[4].toString() == null ");
//            }
//
//            logger.info("   3333");
//            if (args[4] == "null") {
//                logger.info(" !! args[4].toString()  == BRAckedNull ");
//            }
//            logger.info("   222 ");
//            if (args[4].equals("null")) {
//                logger.info(" !! args[4].toString().equals(null) ");
//            }

            logger.info(" !! " + opr1 + " ##  " + args[4]);
            if (opr1 == args[4] || opr1.equalsIgnoreCase(args[4])) {
                res = "Yes";
            } else {
                res = "No";
            }
        } catch (Exception e) {
            logger.error("" + e);
        } finally {
            try {
                result.close();
                stmt.close();
            } catch (Exception ex) {
                logger.error("Error" + ex);
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
                    String fileString = args[15] + ",Error Code :CON_RULE_0032 , Error Description : Device serial number does not match with value provided at the time of blocking  ";
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
