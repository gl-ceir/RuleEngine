package com.gl.rule_engine.rules;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author maverick
 */
public class NonGSMACompliantIMEI implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(NonGSMACompliantIMEI.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String res = "No";
        // IMEI is GSMA TAC Compliant[SG1]  from mobile_device_repository
        String query = "select count(*) from  " + ruleEngine.app + ".national_whitelist where imei like '" + ruleEngine.imei + "%' ";
        try (Statement stmt = ruleEngine.connection.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                res = "Yes";
            }
            logger.info("value from db " + res);
        } catch (Exception e) {
            logger.error("Error.." + e);
        }
        return res;
    }

    @Override
    public String executeAction(RuleInfo ruleEngine) {
        try {
            logger.debug("Action::: " + ruleEngine.action);
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
                    String fileString = ruleEngine.fileArray + " ,Error Code :CON_RULE_0019, Error Description : IMEI/ESN/MEID is already present in the system  ";
                    ruleEngine.bw.write(fileString);
                    ruleEngine.bw.newLine();

                }
                break;
                case "Report": {
                    logger.debug("Action is Report");

                }
                break;
                case "Report2": {
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
