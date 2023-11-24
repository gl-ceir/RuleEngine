/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine.rules;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gl.rule_engine.RuleEngine;
import com.gl.rule_engine.RuleEngineInterface;

/**
 *
 * @author maverick
 */
public class TEST_IMEI implements RuleEngineInterface{

    static final Logger logger = LogManager.getLogger(TEST_IMEI.class);
    
    @Override
    public String executeRule(RuleEngine ruleEngine) {
        String value = ""; //  001 / 0044
        String res = "No";
        String query = "select value from sys_param where tag= 'TEST_IMEI_SERIES' ";
        try (Statement stmt = ruleEngine.connection.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                value = rs.getString("value");
            }
            logger.info("Test imei vals from db " + value);
            String[] arr = value.split(",");
            for (String testImeiSeries : arr) {
                if (ruleEngine.imei.startsWith(testImeiSeries)) {
                    res = "Yes";
                }
            }
            logger.info("Is imei : " + ruleEngine.imei + " A Test imei : " + res);
        } catch (Exception e) {
            logger.error("Error.." + e);
        }
        return res;
    }
    @Override
    public String executeAction(RuleEngine ruleEngine) {
        try {
            logger.debug("Action::: " + ruleEngine.action);
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
                case "Report": {
                    logger.debug("Action is Report");
                    try (Statement stmt = ruleEngine.connection.createStatement();) {

                        String qur = " insert into test_imei_details  (imei ,IMSI,  msisdn , record_type , system_type , source,raw_cdr_file_name,imei_arrival_time ,operator, file_name , created_on , modified_on    )  values "
                                + "('" + ruleEngine.imei + "' , '" + ruleEngine.imsi + "', '" + ruleEngine.msisdn + "' ,'" + ruleEngine.recordType + "' , '" + ruleEngine.systemType + "',  '" + ruleEngine.source + "', '" + ruleEngine.rawCdrFileName + "', '" + ruleEngine.imeiArrivalTime + "', '" + ruleEngine.operator + "',   '" + ruleEngine.fileName + "', now(),  now()   ) ";
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
                    logger.debug(" The Action " + ruleEngine.action + "  is Not Defined  ");
            }

            return "Success";
        } catch (Exception e) {
            logger.debug(" Error " + e);
            return "Failure";
        }
    }

}
