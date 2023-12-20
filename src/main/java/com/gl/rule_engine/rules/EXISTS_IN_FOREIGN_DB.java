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

/**
 *
 * @author user
 */
public class EXISTS_IN_FOREIGN_DB implements  ExecutionInterface {

     static final Logger logger = LogManager.getLogger(EXISTS_IN_FOREIGN_DB.class);

     @Override
     public String executeRule(RuleInfo ruleEngine)  {
          String res = "";
          Statement stmt2 = null;
          ResultSet result1 = null;
          try {

               stmt2 = ruleEngine.connection.createStatement();

               String qry = "select count(regularize_device_db.nid) from regularize_device_db inner join end_user_info on end_user_info.nid=regularize_device_db.nid where "
                       + " first_imei='" + ruleEngine.imei + "' or second_imei='" + ruleEngine.imei + "' or third_imei='" + ruleEngine.imei + "' or fourth_imei='" + ruleEngine.imei + "' and nationality<>'Cambodian'  ";
               logger.debug(" Foreign Db " + qry);
               result1 = stmt2.executeQuery(qry);

               int res2 = 0;
               try {
                    while (result1.next()) {
                         res2 = result1.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("");
               }
               if (res2 != 0) {
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

                         String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0018, Error Description : IMEI/ESN/MEID is already present in the system  ";
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
