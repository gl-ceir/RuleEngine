/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.gl.rule_engine.ExecutionInterface;

/**
 *
 * @author user
 */
public class EXIST_IN_OPERATOR_DB implements  ExecutionInterface {

     static final Logger logger = LogManager.getLogger(EXIST_IN_OPERATOR_DB.class);
       @Override
     public String executeRule(RuleInfo ruleEngine)  {
          Statement stmt = null;
          ResultSet result2 = null;
          String res = "";
          try {
               int count1 = 0;
               stmt = ruleEngine.connection.createStatement();
               result2 = stmt.executeQuery("select  count(imei_esn_meid)   from DEVICE_OPERATOR where imei_esn_meid='" + ruleEngine.imei + "' ");
               logger.debug("Qury ..select (imei_esn_meid ) from device_operator_db where imei_esn_meid='" + ruleEngine.imei + "' ");
               try {
                    while (result2.next()) {
                         count1 = result2.getInt(1);
                    }
               } catch (Exception e) {
                    logger.error("E2." + e);
               }
               if (count1 != 0) {
                    res = "YES";
               } else {
                    res = "NO";
               }
          } catch (Exception e) {
               logger.error("E2." + e);
          } finally {
               try {
                    result2.close();
                    stmt.close();
               } catch (Exception e) {
                    logger.info("  " + e);
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
 
                         logger.debug("Operator  Action is Reject");
                         String errmsg = "  , Error Code :CON_RULE_0026 , Error Description :     IMEI is  not Present. (It is not marked as Block ) ";
                         if ( ruleEngine.featureName.equalsIgnoreCase("block")) {
                              errmsg = "  , Error Code :CON_RULE_0027 , Error Description :    IMEI/ESN/MEID is already in process of Blocking. ";
                         }
                         String fileString = ruleEngine.fileArray + " " + errmsg;
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
               logger.error("Error.." + e);
          }
          return "Failure";

     }

}

//            String url = System.getenv("ceir_db_url");
//            String username = System.getenv("ceir_db_username");
//            String password = System.getenv("ceir_db_password");
//            String db_name = System.getenv("ceir_db_dbName");
//            String db_type = System.getenv("ceir_db_dbType");
//            String jdbcUrlO = "jdbc:oracle:thin:@" + url + "/" + db_name;
//            String classNameO = "oracle.jdbc.driver.OracleDriver";
//            String jdbcUrlM = "jdbc:mysql://" + url + "/" + db_name;
//            String classNameM = "com.mysql.cj.jdbc.Driver";
//
//             
//           
//            if (db_type.equalsIgnoreCase("oracle")) {
//                className = classNameO;
//                jdbcUrl = jdbcUrlO;
//            } else {
//                className = classNameM;
//                jdbcUrl = jdbcUrlM;
//            }
//
//             
//            if (ruleEngine.featureName.equalsIgnoreCase("CDR")) {
//                logger.debug(" LBD exACTION CDR");
//                Connection  
//                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 0);
//                String date = dateFormat1.format(cal.getTime());
//                String counted = " select count(*) from  device_usage_db   where  imei =   '" + ruleEngine.imei + "'    ";
//                int count = 0;
//                Statement stmtc = ruleEngine.connection.createStatement();
//                ResultSet resultc = stmtc.executeQuery(counted);
//                try {
//                    while (resultc.next()) {
//                        count = resultc.getInt(1);
//                    }
//                } catch (Exception e) {
//                    logger.debug("E2." + e);
//                }
//                String historyIns = "";
//                if (count == 0) {
//                    historyIns = " insert into device_usage_db  (   imei ,created_on, failed_rule_id  , failed_rule_name , msisdn  ,action  ) "
//                            + "values  ( '" + ruleEngine.imei + "' , '" + date + "' ,(select id from rule_engine where name =  '" + args[0] + "' )    , '" + args[0] + "'  , '" + ruleEngine.msisdn + "' , '" + ruleEngine.action + "' ) ";
//
//                } else {
//                    historyIns = "   update device_usage_db set imei = '" + ruleEngine.imei + "'  ,created_on =  '" + date + "', failed_rule_id = (select id from rule_engine where name =  '" + args[0] + "' )  , failed_rule_name  = '" + args[0] + "', msisdn = '" + ruleEngine.msisdn + "' ,action = '" + ruleEngine.action + "'   where  imei = '" + ruleEngine.imei + "'    ";
//
//                }
//
//                PreparedStatement statementN = ruleEngine.connection.prepareStatement(historyIns);
//                logger.debug("Qury.." + historyIns);
//                int rowsInserted1 = statementN.executeUpdate();
//                if (rowsInserted1 > 0) {
//                    logger.debug("inserted/updated in device_usage_db ");
//                }
//
//                String stln = " insert into stolen_track_db (created_on, imei_esn_meid ,device_id_type,file_name,operator_id,record_date,operator_name,device_status    ) values "
//                logger.debug("qury2 .." + stln);
//                PreparedStatement statementq = ruleEngine.connection.prepareStatement(stln);
//                int rowsInserted3 = statementq.executeUpdate();
//                if (rowsInserted3 > 0) {
//                    logger.debug("inserted in stolen_track_db ");
//                }
//                 
//
//            } else
