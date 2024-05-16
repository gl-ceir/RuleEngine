package com.gl;


import com.gl.rule_engine.RuleEngineApplication;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuleEngineAdaptor {

    static final Logger logger = LogManager.getLogger(RuleEngineAdaptor.class);

    public static LinkedHashMap<String, Boolean> startAdaptor(Connection conn, Map device_info) {
        logger.info("Started RuleEngineAdaptor");
        int errorFlag = 0;
        LinkedHashMap<String, String> rule_detail = new LinkedHashMap<String, String>();
        LinkedHashMap<String, Boolean> outputmap = new LinkedHashMap<String, Boolean>();
        String action_output = "";
        ArrayList<RuleAttribute> rulelist = getRuleDetails(conn, device_info);
        try (Statement statement = conn.createStatement()) {
            for (RuleAttribute rule : rulelist) {
                RuleInfo re = createRuleInfo(device_info, rule, conn, statement);
                String output = RuleEngineApplication.startRuleEngine(re); // *********
                logger.info("RuleEngineAdaptor " + rule.rule_name + " : result: " + output);
                if (rule.output.equalsIgnoreCase(output)) {    // go to next rule(  rule_engine _mapping )
                    rule_detail.put("rule_name", null);
                    outputmap.put(rule.rule_name, true);
                } else {
                    outputmap.put(rule.rule_name, false);
                    var ruleaction = createRuleActionInfo(device_info, rule, output, conn, statement);
                    action_output = RuleEngineApplication.startRuleEngine(ruleaction);  // *********
                    if (rule.failed_rule_action.equalsIgnoreCase("rule")) {
                        rule_detail.put("rule_name", null);
                    } else {
                        errorFlag = 1;
                        rule_detail.put("period", rule.period);
                        rule_detail.put("action", rule.action);
                        rule_detail.put("output", rule.output);
                        rule_detail.put("rule_name", rule.rule_name);
                        rule_detail.put("rule_id", rule.ruleid);
                        rule_detail.put("action_output", action_output);
                        rule_detail.put("rule_message", rule.rule_message);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("RuleEngine adaptor :" + e);
        }
        rule_detail.put("errorFlag", String.valueOf(errorFlag));
        return outputmap;
    }


    public static ArrayList getRuleDetails(Connection conn, Map<String, String> device_info) {
        ArrayList rule_details = new ArrayList<RuleAttribute>();
        String period = getPeriodFromSysParam(conn, device_info.get("appdbName")); // write code to get Period
        String query = "select a.id as rule_id,  b.rule_message,   a.name as rule_name,b.output as output,b.grace_action, b.post_grace_action, b.failed_rule_action_grace, b.failed_rule_action_post_grace "
                + " from " + device_info.get("appdbName") + ".rule a, " + device_info.get("appdbName") + ".feature_rule b" +
                " where  a.name=b.name  and  a.state='Enabled' and   b.user_type = '" + device_info.get("userType") + "' and  b.feature='" + device_info.get("feature") + "' " +
                " and (  b." + period + "_action !='NA'   and   b." + period + "_action !='Disabled' )   " +
                "order by b.rule_order asc";
        logger.info("RuleEngine Query :" + query);
        try (Statement stmt = conn.createStatement(); ResultSet rs1 = stmt.executeQuery(query)) {
            while (rs1.next()) {
                var rule = new RuleAttribute(
                        rs1.getString("rule_name"),
                        rs1.getString("output"),
                        rs1.getString("rule_id"),
                        period,
                        rs1.getString(period + "_action"),
                        rs1.getString("failed_rule_action_" + period),
                        rs1.getString("rule_message")
                );
                rule_details.add(rule);
            }
        } catch (Exception e) {
            logger.error("Error" + e);
        }
        return rule_details;
    }

    private static String getPeriodFromSysParam(Connection conn, String appdbName) {
        String query = "select value from " + appdbName + ".sys_param where tag='GRACE_PERIOD_END_DATE'";
        var period = "grace";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                var graceDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("value"));
                if (new Date().compareTo(graceDate) > 0) {
                    period = "post_grace";
                } else {
                    period = "grace";
                }
            }
        } catch (Exception e) {
            logger.error("Error" + e);
        }
        return period;
    }


    private static RuleInfo createRuleInfo(Map<String, String> device_info, RuleAttribute rule, Connection conn, Statement statement) {
        return new RuleInfo(device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"), device_info.get("edrappdbName"),
                device_info.get("imei"), device_info.get("msisdn"), device_info.get("imsi"),
                rule.rule_name, "executeRule", device_info.get("feature"), rule.period, rule.action, device_info.get("operator"), device_info.get("file_name"), conn, statement);
    }

    private static RuleInfo createRuleActionInfo(Map<String, String> device_info, RuleAttribute rule, String output, Connection conn, Statement statement) {
        return new RuleInfo(device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"), device_info.get("edrappdbName"),
                device_info.get("imei"), device_info.get("msisdn"), device_info.get("imsi"),
                rule.rule_name, "executeAction", device_info.get("feature"), rule.period, output.equalsIgnoreCase("NAN") ? "NAN" : rule.action,
                device_info.get("operator"), device_info.get("file_name"), conn, statement);
    }

    private static RuleInfo createRuleActionInfo(Map<String, String> device_info, String fileArray, Connection conn, BufferedWriter bw, String output) {
        return new RuleInfo(device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"), device_info.get("rule_name"), "executeAction",
                device_info.get("feature"), device_info.get("IMEIESNMEID"),
                device_info.get("SNofDevice"), device_info.get("file_name"),
                device_info.get("DeviceType"), device_info.get("operator"), device_info.get("DeviceIdType"), device_info.get("operator_tag"), device_info.get("msisdn"),
                output.equalsIgnoreCase("NAN") ? "NAN" : device_info.get("action"),
                "", "", "", device_info.get("operator"),
                "", "", device_info.get("txn_id"), fileArray, device_info.get("period"), conn, bw);
    }
}


class RuleAttribute {
    String rule_name;
    String output;
    String ruleid;
    String period;
    String action;
    String failed_rule_action;
    String rule_message;

    public RuleAttribute(String rule_name, String output, String ruleid, String period, String action, String failed_rule_action, String rule_message) {
        this.rule_name = rule_name;
        this.output = output;
        this.ruleid = ruleid;
        this.period = period;
        this.action = action;
        this.failed_rule_action = failed_rule_action;
        this.rule_message = rule_message;
    }
}


/*
Map.of("appdbName","",
                "auddbName","",
                "repdbName","",
                "edrappdbName","",
                "imei","",
                "msisdn","",
                "imsi","",
                "feature","",
                "operator","",
                "file_name",""
                );


      return new RuleInfo(device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"),
rule.rule_name, "executeRule", device_info.get("feature"), device_info.get("imei").length() > 14 ? device_info.get("imei").substring(0, 14) : device_info.get("imei"),
                device_info.get("SNofDevice"), device_info.get("file_name"),
                device_info.get("DeviceType"), device_info.get("operator"), device_info.get("DeviceIdType"), device_info.get("operator_tag"), device_info.get("msisdn"),
                device_info.get("action"),
                "", "", "", device_info.get("operator"),
                "", "", device_info.get("txn_id"), fileArray, device_info.get("period"), conn, bw);


            // To be req if, file is created.
            //   String fileArray = device_info.get("DeviceType") + "," + device_info.get("DeviceIdType") + "," + device_info.get("MultipleSIMStatus") + "," + device_info.get("SNofDevice") + "," + device_info.get("IMEIESNMEID") + "," + device_info.get("Devicelaunchdate") + "," + device_info.get("DeviceStatus") + "";




 */