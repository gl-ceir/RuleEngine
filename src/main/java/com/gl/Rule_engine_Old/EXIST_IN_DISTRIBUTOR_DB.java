/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine_Old;

import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
class EXIST_IN_DISTRIBUTOR_DB {

    static final Logger logger = LogManager.getLogger(EXIST_IN_DISTRIBUTOR_DB.class);
     


    static String executeRule(String[] args, Connection conn ) {
        String res = "No";
//        logger.debug("EXIST_IN_DISTRIBUTOR_DB executeRule");
       
        try {
           Statement stmt2 = conn.createStatement();
           
            {
                ResultSet result1 = stmt2.executeQuery("select count(imei_esn_meid) from device_distributor_db  where imei_esn_meid='" + args[3] + "' ");
                logger.debug("select count(*) from device_distributor_db  where imei_esn_meid='" + args[3] + "' ");
                 int res2 =  0;
                    try {
                    while (result1.next()) {
                        res2 = result1.getInt(1);
                    }
                } catch (Exception e) {
                    logger.debug("error " +e);
                }
                if (res2 !=0 ) {
                    res = "Yes";
                } else {
                    res = "No";
                }
                result1.close();
             stmt2.close();
            }
             
        } catch (Exception e) {
            logger.debug("error.." + e);
        }
        return res;
    }

    static String executeAction(String[] args, Connection conn , BufferedWriter bw) {
        try {
            switch (args[13]) {
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
                String fileString = args[15] + " ,Error Code :CON_RULE_0007,  Error Description : IMEI/ESN/MEID is already present in the system  ";
                   bw.write(fileString);
                bw.newLine();
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
                logger.debug(" The Action " + args[13] + "  is Not Defined  ");
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
//    
//    static String executeAction(String[] args, Connection conn , BufferedWriter bw) {
//        String rrst = "Success";
//        try {
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
////            if (args[11].equalsIgnoreCase("grace")) {
////                Connection  
////                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
////                Calendar cal = Calendar.getInstance();
////                cal.add(Calendar.DATE, 0);
////                String date = dateFormat1.format(cal.getTime());
////                String historyIns = " insert into device_distributor_db (created_on,device_id ,device_type,   device_status , imei_esn_meid   ) values  ( '" + date + "'  , 10 , '" + args[3] + "' ) ";
////                logger.debug("qry..."+historyIns);
////                PreparedStatement statementN = conn.prepareStatement(historyIns);
////                int rowsInserted1 = statementN.executeUpdate();
////                if (rowsInserted1 > 0) {
////                    logger.debug(" Details inserted into device_distributor_db for Grace Period");
////                    
////                }
////       //                String deviDB = " insert into device_db (created_on,device_id ,device_type   device_status , imei_esn_meid   ) values  ( '" + date + "'  , 10 , '" + args[3] + "' ) ";
//////                PreparedStatement statementdeviDB = conn.prepareStatement(deviDB);
//////                int rowsInserteddeviDB = statementdeviDB.executeUpdate();
//////                if (rowsInserteddeviDB > 0) {
//////                    logger.debug("inserted into device_db");
//////                }
//////                
////                 
////            } 
//            
//            
//            {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", args[14]);
//                String fileString =args[15]  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
//                map.put("fileString", fileString);
//                   bw.write(fileString);
                
//                 return "Success";
//        } catch (Exception e) {
//            rrst = "Error";
//        }
//        return rrst;
//    }
//
//}
