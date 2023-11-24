/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
import java.sql.Connection;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.gl.rule_engine.ExecutionInterface;

 
public class EXIST_IN_BLACKLIST_DB implements  ExecutionInterface {

    static final Logger logger = LogManager.getLogger(EXIST_IN_BLACKLIST_DB.class);

    @Override
     public String executeRule(RuleInfo ruleEngine)  {
        String res = "";
        logger.debug("EXIST_IN_BLACKLIST_DB executeRule");
        Statement stmt2 = null;
        ResultSet result1 = null;
        try {
            stmt2 = ruleEngine.connection.createStatement();
            String qur = " select count(imei) from app.blacklist  where imei  =   '" + ruleEngine.imei + "'  ";
            logger.debug("Query:  " + qur);

            result1 = stmt2.executeQuery(qur);
            String res2 = "0";
            try {
                while (result1.next()) {
                    res2 = result1.getString(1);
                }
            } catch (Exception e) {
                logger.debug("Error " + e);
            }
            if (!res2.equals("0")) {
                res = "Yes";
            } else {
                res = "No";
            }

        } catch (Exception e) {
            logger.error("error.." + e);

        } finally {
            try {
                result1.close();
                stmt2.close();
            } catch (Exception ex) {
                logger.error("Error" + ex);
            }
        }
        return res;
    }

    @Override
     public String executeAction(RuleInfo ruleEngine)  {
        try {
            switch (ruleEngine.action) {
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
                    String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0017, Error Description : IMEI/ESN/MEID is already present in the system  ";
                    ruleEngine.bw.write(fileString);
                    ruleEngine.bw.newLine();
                }
                break;
                case "Block": {
                    logger.debug("Action is Block");
                    
                    try {
                        Statement stmt = ruleEngine.connection.createStatement();
                        String qur = " insert into blocked_device_db  (imei ,IMSI,  msisdn , record_type , system_type , source,raw_cdr_file_name,imei_arrivalTime ,operator, file_name , created_on , modified_on    )  values "
                                + "('" + ruleEngine.imei + "' , '" + ruleEngine.imsi + "', '" + ruleEngine.msisdn + "' ,'" + ruleEngine.recordType + "' , '" + ruleEngine.systemType + "',  '" + ruleEngine.source + "', '" + ruleEngine.rawCdrFileName + "', '" + ruleEngine.imeiArrivalTime + "', '" + ruleEngine.operator + "',   '" + ruleEngine.fileName + "', current_timestamp,  current_timestamp   ) ";
                     logger.info(".." +  qur);
                        stmt.executeUpdate(qur);
                        stmt.close();
                    } catch (Exception e) {
                        logger.debug("Error " + e);
                    }

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
                    logger.debug(" The Action " + ruleEngine.action + "  is Not Defined  ");
            }

            return "Success";
        } catch (Exception e) {
            logger.debug(" Error " + e);
            return "Failure";
        }
    }

}
