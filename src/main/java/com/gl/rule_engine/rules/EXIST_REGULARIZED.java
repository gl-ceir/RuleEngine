package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleEngine;
import com.gl.rule_engine.RuleEngineInterface;
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
public class EXIST_REGULARIZED implements  RuleEngineInterface{

    static final Logger logger = LogManager.getLogger(EXIST_REGULARIZED.class);

    @Override
     public String executeRule(RuleEngine ruleEngine)  {
        String str = "";
        logger.debug("EXIST_REGULARIZED executeRule ");
          Statement stmt2 = null;
          ResultSet result1 = null;
        {
            try {
                  stmt2 = ruleEngine.connection.createStatement();
                  result1 = stmt2.executeQuery("select action, msisdn from device_usage_db  where imei_esn_meid='" + ruleEngine.imei + "' ");
                logger.debug("select action, msisdn from device_usage_db  where imei_esn_meid='" + ruleEngine.imei + "' ");
                String actn = "";
                String msdn = "";
                try {
                    while (result1.next()) {
                        actn = result1.getString("action");
                        msdn = result1.getString("msisdn");
                    }
                } catch (Exception e) {
                    logger.error("" + e);
                }
                logger.debug("actn " + actn + "... msdn.." + msdn);
                if (actn.equalsIgnoreCase("2") || actn.equalsIgnoreCase("0")) {
                    if (msdn.equalsIgnoreCase(ruleEngine.msisdn)) {
                        str = "No";
//                        logger.debug("No");
                    } else {
                        str = chckDubplicateDb(ruleEngine);
                    }
                } else {
                    str = chckDubplicateDb(ruleEngine);
                } 
            } catch (Exception e) {
                logger.error("Erroer" + e);
            }
            finally {
               try {
                    result1.close();
                    stmt2.close();
               } catch (Exception ex) {
                    logger.error("Error" + ex);
               }
          }
        }
        return str;
    }

    public   String chckDubplicateDb(RuleEngine ruleEngine) {
        logger.debug("Chcking for Dupblicate");
        String res = "";

        try {

            Statement stmt3 = ruleEngine.connection.createStatement();
            ResultSet result3 = stmt3.executeQuery("select action from device_duplicate_db  where imei_esn_meid='" + ruleEngine.imei + "'    and msisdn = '" + ruleEngine.msisdn + "' ");
            logger.debug("select action from device_duplicate_db  where imei_esn_meid='" + ruleEngine.imei + "'    and msisdn = '" + ruleEngine.msisdn + "' ");
            String actn3 = "";
            try {
                while (result3.next()) {
                    actn3 = result3.getString(1);
                }
            } catch (Exception e) {
                logger.debug("");
            }
            if (actn3.equalsIgnoreCase("2") || actn3.equalsIgnoreCase("0")) {

                res = "No";

            } else {
                res = "Yes";
            }
            result3.close();
            stmt3.close();
        } catch (Exception e) {
            logger.error("Error .." + e);
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

                    String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0022, Error Description : IMEI/ESN/MEID is already present in the system  ";

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
