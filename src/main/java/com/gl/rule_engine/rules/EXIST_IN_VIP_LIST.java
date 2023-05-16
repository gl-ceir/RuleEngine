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
public class EXIST_IN_VIP_LIST implements  RuleEngineInterface{

     static final Logger logger = LogManager.getLogger(EXIST_IN_VIP_LIST.class);

     @Override
     public String executeRule(RuleEngine ruleEngine)  {
          String res = "";
           Statement stmt2 = null;
          ResultSet result1 = null;
          try {
                 stmt2 = ruleEngine.connection.createStatement();
               String qury = "select count(imei)  from vip_list  where IMEI='" + ruleEngine.imei + "' ";
                 result1 = stmt2.executeQuery(qury);
               logger.debug(qury);
               int res1 = 0;
               try {
                    while (result1.next()) {
                         res1 = result1.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("" + e);
               }
               if (res1 != 0) {
                    res = "Yes";
               } else {
                    res = "no";
               }
               result1.close();
               stmt2.close();
          } catch (Exception e) {
               logger.debug("" + e);
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

                         String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0015,Error Description : IMEI/ESN/MEID is Vip ";
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

//    public String executeAction(String[] args, Connection conn , BufferedWriter bw) {
//        logger.debug("Skip the action");
//        return "Skip";
//
////              Map<String, String> map = new HashMap<String, String>();
////            map.put("fileName", ruleEngine.txn_id);
////           String fileString =ruleEngine.fileArray  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
////  map.put("fileString", fileString);
////               ruleEngine.bw.write(fileString);
//    }
}


  //select count(regularize_device_db.nid) from regularize_device_db inner join end_userdb on end_userdb.nid=regularize_device_db.nid where (first_imei='1234567890123456' or second_imei='1234567890123456' or third_imei='1234567890123456' or fourth_imei='1234567890123456') and nationality<>'Cambodian';
