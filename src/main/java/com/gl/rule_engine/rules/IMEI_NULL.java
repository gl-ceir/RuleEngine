/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.RuleInfo;
import java.sql.Connection;
import java.io.BufferedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.gl.rule_engine.ExecutionInterface;

/**
 *
 * @author user
 */
public class IMEI_NULL implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(IMEI_NULL.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String res = "";
        try {
            if ((ruleEngine.imei == null) || ruleEngine.imei == "" || ruleEngine.imei.contains("00000000")) {
                res = "Yes";
            } else {
                res = "No";
            }
        } catch (Exception e) {
            logger.error("Error.." + e);
        }
        return res;
    }

    @Override
    public String executeAction(RuleInfo ruleEngine) {
        String res = "Success";
        try {
            {
                String fileString = ruleEngine.fileArray + ", Error Code :CON_RULE_0014,Error Description:IMEI/ESN/MEID is missing in the record. ";
                ruleEngine.bw.write(fileString);
                ruleEngine.bw.newLine();
            }
        } catch (Exception e) {
            logger.debug("Error e ");
            res = "Error";
        }
        return res;
    }

}
