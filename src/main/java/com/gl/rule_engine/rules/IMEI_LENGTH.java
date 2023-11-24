/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
import java.sql.Connection;
import java.io.BufferedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.gl.rule_engine.ExecutionInterface;

/**
 *
 * @author user
 */
public class IMEI_LENGTH implements  ExecutionInterface{

     static final Logger logger = LogManager.getLogger(IMEI_LENGTH.class);

     @Override
     public String executeRule(RuleInfo ruleEngine) {
          String res = "Yes";
          System.out.println("IMEI_LENGTH executeRule ....." +ruleEngine.imei);
          try {

               if (ruleEngine.deviceIdType.trim().equalsIgnoreCase("IMEI") || ruleEngine.operatorTag.trim().equalsIgnoreCase("GSM")) {
                    logger.debug(".. " + ruleEngine.deviceIdType);
                    if ((ruleEngine.imei.length() == 15 || ruleEngine.imei.length() == 16) && (ruleEngine.imei.matches("^[0-9]+$"))) {
                         res = "Yes";
                    } else {
                         res = "No";
                    }
               } else {
                    logger.debug("... ");
                    if (ruleEngine.deviceIdType.trim().equalsIgnoreCase("MEID")) {
                         logger.debug(".... ");
                         if ((ruleEngine.imei.length() == 15 || ruleEngine.imei.length() == 16) && (ruleEngine.imei.matches("^[0-9A-F]+$"))) {
                              res = "Yes";
                         } else {
                              res = "No";
                         }
                    } else if (ruleEngine.deviceIdType.trim().equalsIgnoreCase("ESN")) {

                      
                         switch (ruleEngine.imei.length()) {
                              case 8:
                                   if (ruleEngine.imei.matches("^[0-9A-F]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;
                              case 11:
                                   if (ruleEngine.imei.matches("^[0-9]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;
                              default:
                                   res = "No";
                                   break;
                         }
                    } else {   ///cdma

                         logger.debug("...... ");
                         switch (ruleEngine.imei.length()) {
                              case 8:
                                   if (ruleEngine.imei.matches("^[0-9A-F]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;

                              case 15:
                                   if (ruleEngine.imei.matches("^[0-9A-F]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;

                              case 16:
                                   if (ruleEngine.imei.matches("^[0-9A-F]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;

                              case 11:
                                   if (ruleEngine.imei.matches("^[0-9]+$")) {
                                        res = "Yes";
                                   } else {
                                        res = "No";
                                   }
                                   break;
                              default:
                                   res = "No";
                                   break;
                         }

                    }

               }

               // cdma  meid/esn  ..      gsm imei
          } catch (Exception e) {
               logger.error("Error.." + e);
          } finally {
               logger.debug("Imei Length Finished");
          }
          return res;
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

                         String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0012, Error Description : IMEI/ESN/MEID is not as per Specifications ";
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

//                    Connection  
//                    boolean isOracle = ruleEngine.connection.toString().contains("oracle");
//                    String dateFunction = Util.defaultDate(isOracle);
//                    String qry1 = " insert into device_invalid_db (imei ,   failedrule, failedruleid, action, failed_rule_date  ) values  (  '" + ruleEngine.imei + "'  ,  'IMEI_LENGTH'  , ( select id from rule_engine where name =   'IMEI_LENGTH' ), '" + actn + "' , " + dateFunction + " ) ";
//                    logger.debug("" + qry1);
//                    PreparedStatement statement1 = ruleEngine.connection.prepareStatement(qry1);
//                    int rowsInserted11 = statement1.executeUpdate();
//                    if (rowsInserted11 > 0) {
//                        logger.debug("inserted into device_invalid_db tabl");
//                    }
//                     
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
               logger.debug(" Error " + e);
               return "Failure";
          }
     }
 
}

//
//
//IMEI — numeric, between 0-9…
//MEID - alphanumeric, between 0-9-A-F…
//Both are 15 digit or 16 digits
//If the number is ESN, the length is either 8 or 11. If length is 11, then all value should be between 0-9.
//if length is 8, then value would be between 0-9,A-F
//In case of IMEI, it is called TAC, in case of MEID it is called Manufacturer code.
