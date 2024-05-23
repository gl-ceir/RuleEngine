/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;


public class GSMA_TYPE_APPROVED implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(GSMA_TYPE_APPROVED.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String query = "select * from app.mobile_device_repository where device_id='" + ruleEngine.imei.substring(0, 8) + "' and is_type_approved=1";
        logger.debug("Query " + query);
        var response = "NO";
        try ( ResultSet rs = ruleEngine.statement.executeQuery(query)) {
            while (rs.next()) {
                response = "YES";
            }
        } catch (Exception e) {
            logger.error(e + ", [QUERY]" + query);
        }
        return response;
    }

    @Override

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
                    String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0003 , Error Description :TAC in IMEI is not approved TAC from GSMA Tac Details ";
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
                    String fileString = ruleEngine.fileArray + " , Error Code :CON_RULE_0002, Error Description :Could not ruleEngine.connectionect to GSMA server.Try after Some Time.  ";
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
