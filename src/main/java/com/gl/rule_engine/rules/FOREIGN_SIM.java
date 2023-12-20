/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
import com.gl.utils.Util;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.gl.rule_engine.ExecutionInterface;

public class FOREIGN_SIM implements  ExecutionInterface{

     static final Logger logger = LogManager.getLogger(FOREIGN_SIM.class);

     
    //       if (!(((ruleEngine.msisdn.trim().startsWith("19") || ruleEngine.msisdn.trim().startsWith("00")) 
//                        ? ruleEngine.msisdn.substring(2) : ruleEngine.msisdn)).startsWith("855") 
//                         || !args[11].trim().startsWith("456")) {
//                    return "No";
//                } else { 
//                    return "Yes";
//                } 
     @Override
     public String executeRule(RuleInfo ruleEngine)  {
          String res = "No";
          try {
               String msisdn = ruleEngine.msisdn.startsWith("19") ? ruleEngine.msisdn.substring(2) : ruleEngine.msisdn;
               if (msisdn.startsWith("855")) { 
                    res = "Yes";
               } else {
                    res = "No";
               }
          } catch (Exception e) {
               logger.error("Error.." + e);
          } finally {
               logger.debug("");
          }
          return res;
     }

     @Override
     public String executeAction(RuleInfo ruleEngine)  {

          try {
               switch (ruleEngine.action) {
                    case "Allow": {
//                         String msisdn = ruleEngine.msisdn.startsWith("19") ? ruleEngine.msisdn.substring(2) : ruleEngine.msisdn;
                         logger.debug("Action is Allow");
//                         ResultSet rs1 = null;
//                         ResultSet rs = null;
//                         Statement stmt = null;
//                         try {
//                              boolean isOracle = ruleEngine.connection.toString().contains("oracle");
//                              String dateFunction = Util.defaultDate(isOracle);
//                              int status = 0;
//                              String my_query = "";
//                              String query = "select * from foreign_sim_msisdn_db where msisdn='" + msisdn + "' and imei_esn_meid = '" + ruleEngine.imei + "' ";
//                              logger.debug("foreign_sim_msisdn_db  " + query);
//                              stmt = ruleEngine.connection.createStatement();
//                              rs1 = stmt.executeQuery(query);
//                              int count = 0;
//                              while (rs1.next()) {
//                                   status = 1;
//                                   count = rs1.getInt("msisdn_count");
//                              }
//                              rs1.close();
//                              if (status == 0) {
//                                   my_query = "insert into foreign_sim_msisdn_db (created_on , updated_on , operator, file_name ,imei_esn_meid , msisdn_count ,msisdn  )"
//                                           + " values (" + dateFunction + " , " + dateFunction + " , '" + args[8] + "',  '" + args[5] + "',  '" + ruleEngine.imei + "',  1,  '" + msisdn + "'    ) ";
//                              } else {
//                                   my_query = " update   foreign_sim_msisdn_db set    file_name = '" + args[5] + "' , operator  = '" + args[8] + "' , updated_on = " + dateFunction + "  ,msisdn_count = " + (count + 1) + "     where   msisdn='" + msisdn + "' and imei_esn_meid = '" + ruleEngine.imei + "'     ";
//                              }
//                              logger.debug("  " + my_query);
////                                       stmt = ruleEngine.connection.createStatement();     // uncomment this if not working 
//                              rs = stmt.executeQuery(my_query);
//                         } catch (Exception e) {
//                              logger.error("Error e " + e);
//                         } finally {
//                              rs.close();
//                              rs1.close();
//                              stmt.close();
//                         }

                    }
                    break;
                    case "Skip": {
                         logger.debug("Action is Skip");
                    }
                    break;
                    case "Reject": {
                         logger.debug("Action is Reject");
                         String fileString = ruleEngine.fileArray + " ,   Error Code :CON_RULE_0028 ,   Error Description : Imei  Utilised By Foreign Sim   ";
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
//                try {
//                    String actn = "";
//
//                    if (args[11].equalsIgnoreCase("grace")) {
//                        actn = "0";
//                    } else {
//                        actn = "1";
//                    }
//                    logger.debug("Action ..." + actn);
//                    Connection  
//                    boolean isOracle = ruleEngine.connection.toString().contains("oracle");
//                    String dateFunction = Util.defaultDate(isOracle);
//                    String qry1 = " insert into device_invalid_db (imei ,   failedrule, failedruleid, action, failed_rule_date  ) values  (  '" + ruleEngine.imei + "'  ,  'IMEI_LENGTH'  , ( select id from rule_engine where name =   'IMEI_LENGTH' ), '" + actn + "' , " + dateFunction + " ) ";
//                    logger.debug("" + qry1);
//                    PreparedStatement statement1 = ruleEngine.connection.prepareStatement(qry1);
//                    int rowsInserted11 = statement1.executeUpdate();
//                    if (rowsInserted11 > 0) {
//                        logger.debug("inserted into device _invalid_db tabl");
//                    }      
//                } catch (Exception e) {
//                    logger.debug("Error e " + e);
//                }
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
                logger.error("Error " + e);
               return "Failure";
          }
     }

}






