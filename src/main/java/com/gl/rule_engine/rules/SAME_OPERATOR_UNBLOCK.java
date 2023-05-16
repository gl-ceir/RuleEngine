/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 


package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleEngine;
import com.gl.rule_engine.RuleEngineInterface;
import java.sql.Connection;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
public class SAME_OPERATOR_UNBLOCK implements  RuleEngineInterface{

    static final Logger logger = LogManager.getLogger(SYS_REG.class);

    @Override
     public String executeRule(RuleEngine ruleEngine)  {
        String res = "";
        
          Statement stmt2 = null;
          ResultSet result1 = null;
        try {
            String opr1 = null;
            String opr2 = null;
              stmt2 = ruleEngine.connection.createStatement();
            String qury = " select OPERATOR_TYPE_ID from stolenand_recovery_mgmt where  TXN_ID = (select TXN_ID  from  device_operator_db where IMEI_ESN_MEID = '" + ruleEngine.imei + "' )";
              result1 = stmt2.executeQuery(qury);
            logger.debug(qury);
            try {
                while (result1.next()) {
                    opr1 = result1.getString(1);
                }
            } catch (Exception e) {
                logger.error("opr1 " + e);
            }

            qury = " select OPERATOR_TYPE_ID from stolenand_recovery_mgmt where  TXN_ID =  '" + ruleEngine.txn_id + "' ";
            result1 = stmt2.executeQuery(qury);
            logger.debug(qury);
            try {
                while (result1.next()) {
                    opr2 = result1.getString(1);
                }
            } catch (Exception e) {
                logger.error("opr2 " + e);
            }

            if (opr1.equals(opr2)) {
                res = "Yes";
            } else {
                res = "No";
            } 
        } catch (Exception e) {
            logger.error("" + e);
        }finally {
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
     public String executeAction(RuleEngine ruleEngine)  {
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

                    String fileString = ruleEngine.fileArray + ",Error Code :CON_RULE_0029 , Error Description : Current Operator don't have Permission to UnBlock this  IMEI/ESN/MEID   ";
                    ruleEngine.bw.write(fileString);
                    ruleEngine.bw.newLine();
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
                    logger.debug(" The Action " + ruleEngine.action + "  is Not Defined  ");
            }

            return "Success";
        } catch (Exception e) {
            logger.debug(" Error " + e);
            return "Failure";
        }
    }

}
