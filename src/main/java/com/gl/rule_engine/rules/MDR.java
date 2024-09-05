package com.gl.rule_engine.rules;

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;

public class MDR implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(MDR.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String query = "select * from  " + ruleEngine.app + ".mobile_device_repository where device_id='" + ruleEngine.imei.substring(0, 8) + "' ";
        logger.debug("Query " + query);
        var response = "NO";
        try (var stt = ruleEngine.connection.createStatement(); ResultSet rs = stt.executeQuery(query)) {
            while (rs.next()) {
                response = "YES";
            }
        } catch (Exception e) {
            logger.error(e.toString() + ", [QUERY]" + query);
        }
        return response;
    }


    @Override
    public String executeAction(RuleInfo ruleEngine) {
        try {
            switch (ruleEngine.action) {
                case "Allow": {
                    logger.info("Action is Allow");
                }
                break;
                case "Skip": {
                    logger.info("Action is Skip");
                }
                break;
                case "Reject": {
                }
                break;
                case "Block": {
                    logger.info("Action is Block");
                }
                break;
                case "Report": {
                    logger.info("Action is Report");
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
