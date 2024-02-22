package com.gl;


import com.gl.rule_engine.RuleEngineApplication;
import com.gl.rule_engine.RuleInfo;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class RuleEngineAdaptor {

    public static HashMap<String, String> startFeatureRule(Connection conn, HashMap<String, String> device_info) {
        int errorFlag = 0;
        HashMap<String, String> rule_detail = new HashMap<String, String>();
        String action_output = "";
        ArrayList<Rule> rulelist = getRuleDetails(conn , device_info);
        for (Rule rule : rulelist) {
            String fileArray = device_info.get("DeviceType") + "," + device_info.get("DeviceIdType") + "," + device_info.get("MultipleSIMStatus") + "," + device_info.get("SNofDevice") + "," + device_info.get("IMEIESNMEID") + "," + device_info.get("Devicelaunchdate") + "," + device_info.get("DeviceStatus") + "";
            RuleInfo re = createRuleInfo(device_info, fileArray, conn, null);
            String output = RuleEngineApplication.startRuleEngine(re);
            // eventWriter(  myWriter ,device_info ,"RuleCheckEnd" , output  );
            if (rule.output.equalsIgnoreCase(output)) {    // go to next rule(  rule_engine _mapping )
                rule_detail.put("rule_name", null);
            } else {
                //  eventWriter(  myWriter ,device_info ,"RuleActionStart" , ""  );
                RuleInfo reAction = createRuleActionInfo(device_info, fileArray, conn, null, output);
                action_output = RuleEngineApplication.startRuleEngine(reAction);
                //    logger.info("Rule Filter Action Output is [" + action_output + "]");
                //   eventWriter(  myWriter ,device_info ,"failed_rule_action :" + device_info.get("failed_rule_aciton") , action_output );
                if (rule.failed_rule_action.equalsIgnoreCase("rule")) {
                    rule_detail.put("rule_name", null);
                    //      logger.info("failed_rule_aciton is Rule ..   ");  // if action FAils But next failed_rule_aciton is Rule , ..action _output  will not work ....
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
        rule_detail.put("errorFlag", String.valueOf(errorFlag));
        return rule_detail;
    }

//    void eventWriter(FileWriter myWriter, HashMap<String, String> device_info, String type, String output) {
//        new LogWriter().writeFeatureEvents(myWriter, device_info.get("IMEIESNMEID"),
//                device_info.get("DeviceType"), device_info.get("MultipleSIMStatus"), device_info.get("SNofDevice"),
//                device_info.get("Devicelaunchdate"), device_info.get("DeviceStatus"),
//                device_info.get("txn_id"), device_info.get("operator"), device_info.get("file_name"), type,
//                device_info.get("ruleid"), device_info.get("rule_name"), output, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
//    }


    private static RuleInfo createRuleInfo(HashMap<String, String> device_info, String fileArray, Connection conn, BufferedWriter bw) {
        return new RuleInfo( device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"), device_info.get("rule_name"), "executeRule", device_info.get("feature"), device_info.get("IMEIESNMEID").length() > 14 ? device_info.get("IMEIESNMEID").substring(0, 14) : device_info.get("IMEIESNMEID"),
                device_info.get("SNofDevice"), device_info.get("file_name"),
                device_info.get("DeviceType"), device_info.get("operator"), device_info.get("DeviceIdType"), device_info.get("operator_tag"), device_info.get("MSISDN"),
                device_info.get("action"),
                "", "", "", device_info.get("operator"),
                "", "", device_info.get("txn_id"), fileArray, device_info.get("period"), conn, bw);
    }

    private static RuleInfo createRuleActionInfo(HashMap<String, String> device_info, String fileArray, Connection conn, BufferedWriter bw, String output) {
        return new RuleInfo(device_info.get("appdbName"), device_info.get("auddbName"), device_info.get("repdbName"),device_info.get("rule_name"), "executeAction", device_info.get("feature"), device_info.get("IMEIESNMEID"),
                device_info.get("SNofDevice"), device_info.get("file_name"),
                device_info.get("DeviceType"), device_info.get("operator"), device_info.get("DeviceIdType"), device_info.get("operator_tag"), device_info.get("MSISDN"),
                output.equalsIgnoreCase("NAN") ? "NAN" : device_info.get("action"),
                "", "", "", device_info.get("operator"),
                "", "", device_info.get("txn_id"), fileArray, device_info.get("period"), conn, bw);

    }

    public static ArrayList getRuleDetails(Connection conn,HashMap<String, String> device_info ) {
        ArrayList rule_details = new ArrayList<Rule>();
        String period = ""; // write code to get Period
        String query = "select a.id as rule_id,  b.rule_message,   a.name as rule_name,b.output as output,b.grace_action, b.post_grace_action, b.failed_rule_action_grace, b.failed_rule_action_post_grace " + " from " + device_info.get("appdbName") + ".rule a, " + device_info.get("appdbName") + ".feature_rule b where  a.name=b.name  and a.state='Enabled' and b.feature='CDR' and   b." + period
                + "_action !='NA' order by b.rule_order asc";
        try (Statement stmt = conn.createStatement(); ResultSet rs1 = stmt.executeQuery(query)) {
            while (rs1.next()) {
                var rule = new Rule(
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
            System.out.println("Error" + e);
        }
        return rule_details;
    }
}

class Rule {
    String rule_name;
    String output;
    String ruleid;
    String period;
    String action;
    String failed_rule_action;
    String rule_message;

    public Rule(String rule_name, String output, String ruleid, String period, String action, String failed_rule_action, String rule_message) {
        this.rule_name = rule_name;
        this.output = output;
        this.ruleid = ruleid;
        this.period = period;
        this.action = action;
        this.failed_rule_action = failed_rule_action;
        this.rule_message = rule_message;
    }
}
