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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
public class TAC_FORMAT implements  RuleEngineInterface{

    static final Logger logger = LogManager.getLogger(TAC_FORMAT.class);

    @Override
     public String executeRule(RuleEngine ruleEngine)  {
        String res = null;
        logger.debug("TAC_FORMAT executeRule ....." + ruleEngine.imei);
        try {
            if ((ruleEngine.imei.length() == 8 && (ruleEngine.imei.matches("^[-\\w.]+")))) {      // args[10].equalsIgnoreCase("GSM") &&
                res = "Yes";
                logger.debug("TAC_FORMAT   ok ");
            } else {
                res = "No";
                logger.debug("TAC_FORMAT   NOT OK ");
            }
        } catch (Exception e) {
            logger.debug("Error.." + e);
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

                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0031 , Error Description :  TAC is not as per specifications  ";

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
//                     
//
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
