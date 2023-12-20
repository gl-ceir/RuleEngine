/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
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
public class EXISTS_IN_WHITELIST_DB implements  ExecutionInterface {

    static final Logger logger = LogManager.getLogger(EXIST_IN_CUSTOM_DB.class);
    @Override
     public String executeRule(RuleInfo ruleEngine)  {
        String res = "";
            Statement stmt2 =null;
          ResultSet result1 = null;
        try {
              stmt2 = ruleEngine.connection.createStatement();
            
            {
                  result1 = stmt2.executeQuery("select count(imei) from white_list  where IMEI='" + ruleEngine.imei + "' ");
            logger.debug("select count(imei) from white_list  where IMEI='" + ruleEngine.imei + "' ");
                  
                  
                  String res2 = "0";
                try {
                    while (result1.next()) {
                        res2 = result1.getString(1);
                    }
                } catch (Exception e) {
                    logger.error("");
                }
                if (!res2.equals("0")) {
                    res = "Yes";
                } else {
                    res = "No";
                }
                result1.close();
                stmt2.close();
            }
        } catch (Exception e) {
            logger.debug("error.." + e);
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
    try{    switch (ruleEngine.action) {
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

                String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0021, Error Description : IMEI/ESN/MEID is already present in the system  ";
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
