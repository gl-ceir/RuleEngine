 

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
public class SAME_DEVICETYPE_UNBLOCK implements  ExecutionInterface{

    static final Logger logger = LogManager.getLogger(SAME_DEVICETYPE_UNBLOCK.class);

    @Override
     public String executeRule(RuleInfo ruleEngine)  {
        String res = "";
        Statement stmt = null;
        ResultSet result = null;
        try {
            String opr1 =null;
         
            stmt = ruleEngine.connection.createStatement();
            String qury = "  select DEVICE_TYPE    from DEVICE_OPERATOR  where imei_esn_meid='" + ruleEngine.imei + "'  ";
            result = stmt.executeQuery(qury);
            logger.debug(qury);
            try {
                while (result.next()) {
                    opr1 = result.getString(1);
                }
            } catch (Exception e) {
                logger.error("opr1 " + e);
            }
  
            logger.info( " !! " + opr1 +  " ##  "+ruleEngine.deviceType  );
            if (opr1 == ruleEngine.deviceType   || opr1.equalsIgnoreCase( ruleEngine.deviceType)    ) {
                res = "Yes";
            } else {
                res = "No";
            }
        } catch (Exception e) {
            logger.error("" + e);
        } finally {
            try {
                result.close();
                stmt.close();
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
                    String fileString = ruleEngine.fileArray + ",Error Code :CON_RULE_0032 , Error Description : Device Type does not match with value provided at the time of blocking  ";
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







