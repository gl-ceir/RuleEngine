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
public class EXIST_IN_END_USER_DB implements  ExecutionInterface {

    static final Logger logger = LogManager.getLogger(EXIST_IN_END_USER_DB.class);
 
    @Override
     public String executeRule(RuleInfo ruleEngine)  {
         
 Statement stmt2 = null;
          ResultSet result1  = null;
        String res = "No"; 
        try {
              stmt2 = ruleEngine.connection.createStatement();
              {
                  result1 = stmt2.executeQuery("select count(imei_esn_meid) from device_end_user_db  where imei_esn_meid='" + ruleEngine.imei + "' ");
               logger.debug("select count(imei_esn_meid) from device_end_user_db  where imei_esn_meid='" + ruleEngine.imei + "' ");
                  int res2 = 0;
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
            }
        } catch (Exception e) {
            logger.error("error.." + e);
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

                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0011, Error Description : IMEI/ESN/MEID is already present in the system  ";

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

//    public String executeAction(String[] args, Connection conn , BufferedWriter bw) {
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
////
////                String historyIns = " insert into device_end_user_db (created_on,device_id ,device_type   device_status , imei_esn_meid   ) values  ( '" + date + "'  , 10 , '" + ruleEngine.imei + "' ) ";
////                PreparedStatement statementN = ruleEngine.connection.prepareStatement(historyIns);
////                int rowsInserted1 = statementN.executeUpdate();
////                if (rowsInserted1 > 0) {
////                    logger.debug("insert into device_end_user_db ");
////                }
////                 
////            } else 
//            {
//
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", ruleEngine.txn_id);
//                String fileString =ruleEngine.fileArray  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
//                map.put("fileString", fileString);
//                   ruleEngine.bw.write(fileString);
              
//                 return "Success";

//            }
//
//        } catch (Exception e) {
//            rrst = "Error";
//        }
//        return rrst;
//    }
//
//}
