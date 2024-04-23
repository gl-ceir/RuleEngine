/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;


public class EXISTS_IN_GREYLIST_DB implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(EXISTS_IN_GREYLIST_DB.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String query = "select * from  " + ruleEngine.app + ".grey_list  where imei ='" + ruleEngine.imei + "' ";
        logger.debug("Query " + query);
        var response = "NO";
        try ( ResultSet rs =ruleEngine.statement.executeQuery(query)) {
            while (rs.next()) {
                response = "YES";
            }
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
        return response;
    }

    @Override
    public String executeAction(RuleInfo ruleEngine) {
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
                    String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0019, Error Description : IMEI/ESN/MEID is already present in the system  ";
                    ruleEngine.bw.write(fileString);
                    ruleEngine.bw.newLine();

                }
                break;
                case "Block": {
                    logger.debug("Action is Block");

                    try {
                        Statement stmt = ruleEngine.connection.createStatement();
                        String qur = " insert into rule_action_block_imei  (imei ,IMSI,  msisdn , record_type , system_type , source,raw_cdr_file_name,imei_arrivalTime ,operator, file_name , created_on , modified_on    )  values "
                                + "('" + ruleEngine.imei + "' , '" + ruleEngine.imsi + "', '" + ruleEngine.msisdn + "' ,'" + ruleEngine.recordType + "' , '" + ruleEngine.systemType + "',  '" + ruleEngine.source + "', '" + ruleEngine.rawCdrFileName + "', '" + ruleEngine.imeiArrivalTime + "', '" + ruleEngine.operator + "',   '" + ruleEngine.fileName + "', current_timestamp,  current_timestamp   ) ";
                        logger.info(".." + qur);
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
