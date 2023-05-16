/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleEngine;
import com.gl.rule_engine.RuleEngineInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gl.Rule_engine_old.BlackList.EncriptonBlacklistService;
import java.sql.Connection;
import java.io.BufferedWriter;

/**
 *
 * @author user
 */
public class EXIST_IN_GSMABLACKLIST_DB implements  RuleEngineInterface {
    static final Logger logger = LogManager.getLogger(EXIST_IN_GSMABLACKLIST_DB.class);
    @Override
     public String executeRule(RuleEngine ruleEngine)  {
        String rslt = EncriptonBlacklistService.startBlacklistApp(ruleEngine.imei, ruleEngine.connection);
        return rslt;
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
                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0017, Error Description :IMEI/ESN/MEID  is Blacklisted By Gsma ";
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
            case "NAN": {
                logger.debug("Action is NAN");
                String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0023, Error Description :System Won't able to establish connection to Gsma Blacklist server. Please try again after Some Time.";
                 ruleEngine.bw.write(fileString);
                ruleEngine.bw.newLine();
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

//create table t1 (
//    c1 NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
//    c2 VARCHAR2(10)
//    );
