/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine.rules;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class EXISTS_IN_GSMA_DETAILS_DB implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(EXISTS_IN_GSMA_DETAILS_DB.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        System.out.println("EXISTS_IN_GSMA_DETAILS_DB works only");
        Statement stmt = null;
        ResultSet result = null;
        String res = "0";
        try {
            stmt = ruleEngine.connection.createStatement();
            String query = "select count(device_id) from app.mobile_device_repository  where device_id='" + ruleEngine.imei.substring(0, 8) + "' ";
            result = stmt.executeQuery(query);
            try {
                while (result.next()) {
                    res = result.getString(1);
                }
            } catch (Exception e) {
                logger.error("");
            }
            if (!res.equals("0")) {
                res = "Yes";
            } else {
                res = "No";
            }
            result.close();
            stmt.close();
        } catch (Exception e) {
            logger.debug("error.." + e);
        } finally {
            try {
                result.close();
                stmt.close();
            } catch (Exception ex) {
                logger.error("Error" + ex);
            }
        }
        return res;
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
