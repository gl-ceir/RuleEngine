package com.gl.rule_engine.rules;

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;

public class NWL_CUSTOM_VALIDITY_FLAG implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(NWL_CUSTOM_VALIDITY_FLAG.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String query = "select  * from  " + ruleEngine.app + ".national_whitelist where validity_flag = 1 and gdce_imei_status =1  and imei like '" + ruleEngine.imei + "%'   ";
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
