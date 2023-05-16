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
 
public class USER_REG implements  RuleEngineInterface{

     static final Logger logger = LogManager.getLogger(USER_REG.class);

     @Override
     public String executeRule(RuleEngine ruleEngine)  {
//        logger.debug(" USER_REG executeRule ");
          String res = "";
          ResultSet result1 = null;
          Statement stmt2 = null;

          try {

               stmt2 = ruleEngine.connection.createStatement();
               String qury = " select action from device_usage_db  where  imei ='" + ruleEngine.imei + "'   union  select action from  device_duplicate_db  where  imei =   '" + ruleEngine.imei + "'  ";
               result1 = stmt2.executeQuery(qury);
               logger.debug(qury);
               Set<String> hash_Set = new HashSet<String>();
               try {
                    while (result1.next()) {
                         hash_Set.add(result1.getString(1));
                    }
               } catch (Exception e) {
                    logger.debug("Error " + e);
               }
               if (hash_Set.contains("USER_REG")) {
                    logger.debug("Yes");
                    res = "Yes";
               } else {
                    logger.debug("No");
                    res = "no";
               }
               result1.close();
               stmt2.close();

          } catch (Exception e) {
               logger.error("Error:" + e);
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
                         String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0032 , Error Description : IMEI/ESN/MEID  is User Registered   ";
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
                         // set action as USER_REG
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

//    public String executeAction(String[] args, Connection conn , BufferedWriter bw) {
//        logger.debug("Skip the action");
//        return "Skip";
//
////              Map<String, String> map = new HashMap<String, String>();
////            map.put("fileName", ruleEngine.txn_id);
////           String fileString =args[1 5]  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
////  map.put("fileString", fileString);
////               ruleEngine.bw.write(fileString);
//    }
}


  //select count(regularize_device_db.nid) from regularize_device_db inner join end_userdb on end_userdb.nid=regularize_device_db.nid where (first_imei='1234567890123456' or second_imei='1234567890123456' or third_imei='1234567890123456' or fourth_imei='1234567890123456') and nationality<>'Cambodian';
