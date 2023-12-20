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
public class EXIST_IN_TAX_PAID_DB implements  ExecutionInterface {

     static final Logger logger = LogManager.getLogger(EXIST_IN_TAX_PAID_DB.class);

     @Override
     public String executeRule(RuleInfo ruleEngine)  {
          logger.debug("EXIST_IN_TAX_PAID_DB executeRule");
          String res = "No";

          Statement stmt2 = null;
          ResultSet result1 = null;
          try {

               stmt2 = ruleEngine.connection.createStatement();
               result1 = stmt2.executeQuery("select count(imei_esn_meid) as cnt  from device_custom_db  where imei_esn_meid='" + ruleEngine.imei + "'  ");
               logger.debug("select count(imei_esn_meid) as cnt  from device_custom_db  where imei_esn_meid='" + ruleEngine.imei + "'  ");
               int res1 = 0;
               try {
                    while (result1.next()) {
                         res1 = result1.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("Error" + e);
               }
               if (res1 != 0) {
                    res = "Yes";
               } else {
                    res = "No";
               }
               result1.close();
               stmt2.close();

          } catch (Exception e) {
               logger.error("" + e);
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

                         String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0016, Error Description : IMEI/ESN/MEID is already present in the system  ";

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
//        logger.debug("exist_in_tax_paid executeAction ");
//        String res = "Success";
//        try {
//            if (ruleEngine.featureName.equalsIgnoreCase("CDR")) {
//                 
//               
//                if (db_type.equalsIgnoreCase("oracle")) {
//                    className = classNameO;
//                    jdbcUrl = jdbcUrlO;
//                } else {
//                    className = classNameM;
//                    jdbcUrl = jdbcUrlM;
//                }
//
//                 
//                Connection  
//                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 0);
//                String date = dateFormat1.format(cal.getTime());
//
//                String historyIns = " update device_usage_db set failed_rule_date = '" + date + "'   ,failed_rule_id = (select id from rule_engine where name = 'EXIST_IN_TAX_PAID_DB'),  failed_rule_name ='EXIST_IN_TAX_PAID_DB' , action = 'USER' ";
//                logger.debug(" is " + historyIns);
//                PreparedStatement statementN = ruleEngine.connection.prepareStatement(historyIns);
//                int rowsInserted1 = statementN.executeUpdate();
//                if (rowsInserted1 > 0) {
//                    logger.debug("device_usage_db updated");
//                }
//                 
//            } else {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", ruleEngine.txn_id);
//                String fileString =ruleEngine.fileArray  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
//                map.put("fileString", fileString);
//                   ruleEngine.bw.write(fileString);
//            }
//
//        } catch (Exception e) {
//            logger.debug("" + e);
//            res = "Error";
//        }
//        return res;
//
//    }
}

//else {
//                Statement stmt3 = ruleEngine.connection.createStatement();
//                ResultSet result3 = stmt3.executeQuery("select count(*) as cnt  from device_db  where imei_esn_meid='" + ruleEngine.imei + "' "
//                        + "  and manufacturer_device_status = 'approved' ");
//
//                logger.debug(" Query ... select count(*) as cnt  from device_db  where imei_esn_meid='" + ruleEngine.imei + "' "
//                        + "  and manufacturer_device_status = 'approved' ");
//
//                int res3 = 0;
//                try {
//                    while (result3.next()) {
//                        res3 = result3.getInt(1);
//                    }
//                } catch (Exception e) {
//                    logger.debug("Error1 " + e);
//                }
//                if (res3 != 0) {
//                    logger.debug("Yes");
//                    res = "Yes";
//                } else {
//                    Statement stmt4 = ruleEngine.connection.createStatement();
//                    ResultSet result4 = stmt4.executeQuery(" select  tax_paid_status from  regularize_device_db  where first_imei= '" + ruleEngine.imei + "' or second_imei  = '" + ruleEngine.imei + "' or fourth_imei= '" + ruleEngine.imei + "' or third_imei = '" + ruleEngine.imei + "'  ");
//
//                    logger.debug("Qry 3 ...select  tax_paid_status from  regularize_device_db  where first_imei= '" + ruleEngine.imei + "' or second_imei  = '" + ruleEngine.imei + "' or fourth_imei= '" + ruleEngine.imei + "' or third_imei = '" + ruleEngine.imei + "'  ");
//                    int res4 = 9;
//                    try {
//                        while (result4.next()) {
//                            res4 = result4.getInt(1);
//                        }
//                    } catch (Exception e) {
//                        logger.debug("");
//                    }
//                    if (res4 == 0) {
//                        logger.debug("Yes");
//                        res = "Yes";
//
//                    } else {
//                        logger.debug("No");
//                        res = "No";
//                    }
//
//                }
//                 
//            }   whoch state in regularize_device_db update
