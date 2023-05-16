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
public class EXISTS_IN_REGULARIZED_DB implements  RuleEngineInterface {

     static final Logger logger = LogManager.getLogger(EXISTS_IN_REGULARIZED_DB.class);

     @Override
     public String executeRule(RuleEngine ruleEngine)  {
          String str = "No";
          logger.debug("EXISTS_IN_REGULARIZED_DB executeRule");
          Statement stmt2 = null;
          ResultSet result1 = null;
          try {

               stmt2 = ruleEngine.connection.createStatement();
               result1 = stmt2.executeQuery("select  tax_paid_status from regularize_device_db where first_imei='" + ruleEngine.imei + "' or second_imei='" + ruleEngine.imei + "' or third_imei='" + ruleEngine.imei + "' or fourth_imei='" + ruleEngine.imei + "' ");
        logger.debug("select  tax_paid_status from regularize_device_db where first_imei='" + ruleEngine.imei + "' or second_imei='" + ruleEngine.imei + "' or third_imei='" + ruleEngine.imei + "' or fourth_imei='" + ruleEngine.imei + "' ");
               String res1 = "No";
               try {
                    while (result1.next()) {
                         res1 = result1.getString(1);
                    }
               } catch (Exception e) {
                    logger.debug("error  + e" + e);
               }

               if (!res1.equals("0")) {
                    res1 = "Yes";
               } else {
                    res1 = "No";
               }

          } catch (Exception e) {
               logger.error("Erroer" + e);
          } finally {
               try {
                    result1.close();
                    stmt2.close();
               } catch (Exception ex) {
                    logger.error("Error" + ex);
               }
          }

          return str;
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

                         String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0013, Error Description : IMEI/ESN/MEID is already present in the system  ";

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

//        
//        
//        try {
//             
//           
//            if (db_type.equals("oracle")) {
//                className = classNameO;
//                jdbcUrl = jdbcUrlO;
//            } else {
//                className = classNameM;
//                jdbcUrl = jdbcUrlM;
//            }
//             
//            Connection  
//            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, 0);
//            String date = dateFormat1.format(cal.getTime());
//
//            String historyIns = " update device_usage_db set failed_rule_date =  '" + date + "'  ,   action ='User' , failed_rule_id = (select id from rule_engine where name = 'EXISTS_IN_REGULARIZED_DB')  , failed_rule_name = 'EXISTS_IN_REGULARIZED_DB' ) ";
//            PreparedStatement statementN = ruleEngine.connection.prepareStatement(historyIns);
//            int rowsInserted1 = statementN.executeUpdate();
//            if (rowsInserted1 > 0) {
//                logger.debug("insert into device_end_user_db ");
//            }
//             
//            return "Success";
//        } catch (Exception e) {
//            logger.debug("error" + e);
//        }
//        return "Success";
//        
//        
//        
//        
//    }
// 
