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
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
public class SYS_REG implements  RuleEngineInterface{

     static final Logger logger = LogManager.getLogger(SYS_REG.class);

     @Override
     public String executeRule(RuleEngine ruleEngine)  {
          String res = "No";
          ResultSet result1 = null;
          Statement stmt2 = null;

          String actnRslt = "";
          String qury = null;
          try {
               stmt2 = ruleEngine.connection.createStatement();
               qury = " select action from active_unique_imei  where  imei ='" + ruleEngine.imei + "'";
              logger.debug("" + qury);
               result1 = stmt2.executeQuery(qury);
               try {
                    while (result1.next()) {
                         actnRslt = result1.getString(1);
                    }
               } catch (Exception e) {
                    logger.error("" + e);
               }
               if (actnRslt.equals("SYS_REG")) {
                    res = "Yes";
               } else {
                    qury = " select action from active_imei_with_different_msisdn  where  imei ='" + ruleEngine.imei + "'";
                    logger.debug("" + qury);
           
                    result1 = stmt2.executeQuery(qury);
                    try {
                         while (result1.next()) {
                              actnRslt = result1.getString(1);
                         }
                    } catch (Exception e) {
                         logger.error("" + e);
                         if (actnRslt.equals("SYS_REG")) {
                              res = "Yes";
                         } else {
                              res = "No";
                         }
                    }
                    result1.close();
                    stmt2.close();
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

                         String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0030 , Error Description : IMEI/ESN/MEID is System Registered ";
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
                     // set action as SYS_REG

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
