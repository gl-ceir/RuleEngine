/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class EXIST_IN_IMPORTER_DB implements  RuleEngineInterface {

    static final Logger logger = LogManager.getLogger(EXIST_IN_IMPORTER_DB.class);


    public String executeRule(RuleEngine ruleEngine ) {
        String res =  "No";
        int res2 =  0;
        logger.debug("EXIST_IN_IMPORTER_DB executeRule");
       
        try {
          
            Statement stmt2 = ruleEngine.connection.createStatement();
            {
                ResultSet result1 = stmt2.executeQuery("select count(imei_esn_meid) from device_importer_db  where imei_esn_meid='" + ruleEngine.imei + "' ");
                logger.debug("select count(imei_esn_meid) from device_importer_db  where imei_esn_meid='" + ruleEngine.imei + "'");
               
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

  public String executeAction(RuleEngine ruleEngine) {
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
                String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0006,  Error Description : IMEI/ESN/MEID is already present in the system  ";
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
