/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

//import com.example.BasicApplication;
 
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gsmaTac.BasicApplication;
import java.sql.Connection;
import java.io.BufferedWriter;
import com.gl.rule_engine.ExecutionInterface;

/**
 *
 * @author user
 */
public class EXISTS_IN_GSMA_TAC_DB implements  ExecutionInterface {     // EXISTS_IN_GSMA_TAC_DB used in TYPE APPROVE TOO

    static final Logger logger = LogManager.getLogger(EXISTS_IN_GSMA_TAC_DB.class);

    @Override
     public String executeRule(RuleInfo ruleEngine)  {
        String res = "No";
        logger.debug("EXISTS_IN_GSMA_TAC executeRule");
        try {
            String tac = "";
          
                tac = ruleEngine.imei.trim().substring(0, 8);
            
            logger.debug("tac val .." + tac);
            if (tac.equalsIgnoreCase("")) {
                res = "No";
            } else {
                logger.debug("GSMA started");
                BasicApplication w = new BasicApplication();
                res = w.gsmaApplication(tac, ruleEngine.connection);
            }
        } catch (Exception e) {
            logger.debug("errror " + e);
        }
        return res;
    }

    public String executeAction(RuleInfo ruleEngine) {
      try{  switch (ruleEngine.action) {
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
                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0003 , Error Description :TAC in IMEI is not approved TAC from GSMA  ";
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
            case "NAN": {
                logger.debug("Action is NAN");
                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0002, Error Description :Could not connect to GSMA server.Try after Some Time.  ";
                 ruleEngine.bw.write(fileString);
                ruleEngine.bw.newLine();
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
 