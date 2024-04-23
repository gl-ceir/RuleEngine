package com.gl.rule_engine;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.Statement;

public class RuleInfo {

    public String app;
    public String aud;
    public String rep;
    public String edrapp;
    public String imei;
    public String msisdn;
    public String imsi;           //txnId
    public String ruleName;
    public String executeRuleAction;   //1 -rule 2  -action
    public String featureName;
    public String period;
    public String action;
    public String operator;
    public String fileName;

    public String sNofDevice;
    public String deviceType;
    public String deviceIdType;      //error
    public String operatorTag;
    public String recordType;  // fileArray
    public String systemType;
    public String source;
    public String rawCdrFileName;
    public String imeiArrivalTime;
    public String txn_id;
    public String fileArray;

    public Connection connection;
    public Statement statement;
    public BufferedWriter bw;


    public RuleInfo() {
    }

    public RuleInfo(String imei, Connection connection) {
        this.imei = imei;
        this.connection = connection;
    }


    public RuleInfo(String app, String aud, String rep, String ruleName, String executeRuleAction, String featureName, String imei, String sNofDevice, String fileName, String deviceType, String operator, String deviceIdType, String operatorTag, String msisdn, String action, String imsi, String recordType, String systemType, String source, String rawCdrFileName, String imeiArrivalTime, String txn_id, String fileArray, String period, Connection connection, BufferedWriter bw) {
        this.app = app;
        this.aud = aud;
        this.rep = rep;
        this.ruleName = ruleName;
        this.executeRuleAction = executeRuleAction;
        this.featureName = featureName;
        this.imei = imei;
        this.sNofDevice = sNofDevice;
        this.fileName = fileName;
        this.deviceType = deviceType;
        this.operator = operator;
        this.deviceIdType = deviceIdType;
        this.operatorTag = operatorTag;
        this.msisdn = msisdn;
        this.action = action;
        this.imsi = imsi;
        this.recordType = recordType;
        this.systemType = systemType;
        this.source = source;
        this.rawCdrFileName = rawCdrFileName;
        this.imeiArrivalTime = imeiArrivalTime;
        this.txn_id = txn_id;
        this.fileArray = fileArray;
        this.period = period;
        this.connection = connection;
        this.bw = bw;
    }


    public RuleInfo(String edrapp, String app, String aud, String rep, String ruleName, String executeRuleAction, String featureName, String imei, String sNofDevice, String fileName, String deviceType, String operator, String deviceIdType, String operatorTag, String msisdn, String action, String imsi, String recordType, String systemType, String source, String rawCdrFileName, String imeiArrivalTime, String txn_id, String fileArray, String period, Connection connection, BufferedWriter bw) {
        this.edrapp = edrapp;
        this.app = app;
        this.aud = aud;
        this.rep = rep;
        this.ruleName = ruleName;
        this.executeRuleAction = executeRuleAction;
        this.featureName = featureName;
        this.imei = imei;
        this.sNofDevice = sNofDevice;
        this.fileName = fileName;
        this.deviceType = deviceType;
        this.operator = operator;
        this.deviceIdType = deviceIdType;
        this.operatorTag = operatorTag;
        this.msisdn = msisdn;
        this.action = action;
        this.imsi = imsi;
        this.recordType = recordType;
        this.systemType = systemType;
        this.source = source;
        this.rawCdrFileName = rawCdrFileName;
        this.imeiArrivalTime = imeiArrivalTime;
        this.txn_id = txn_id;
        this.fileArray = fileArray;
        this.period = period;
        this.connection = connection;
        this.bw = bw;
    }

    public RuleInfo(String app, String aud, String rep, String edrapp, String imei, String msisdn, String imsi, String ruleName, String executeRuleAction, String featureName, String period, String action, String operator, String fileName, Connection connection) {
        this.app = app;
        this.aud = aud;
        this.rep = rep;
        this.edrapp = edrapp;
        this.imei = imei;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.ruleName = ruleName;
        this.executeRuleAction = executeRuleAction;
        this.featureName = featureName;
        this.period = period;
        this.action = action;
        this.operator = operator;
        this.fileName = fileName;
        this.connection = connection;
    }

    public RuleInfo(String app, String aud, String rep, String edrapp, String imei, String msisdn, String imsi, String ruleName, String executeRuleAction, String featureName, String period, String action, String operator, String fileName, Connection connection, Statement statement) {
        this.app = app;
        this.aud = aud;
        this.rep = rep;
        this.edrapp = edrapp;
        this.imei = imei;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.ruleName = ruleName;
        this.executeRuleAction = executeRuleAction;
        this.featureName = featureName;
        this.period = period;
        this.action = action;
        this.operator = operator;
        this.fileName = fileName;
        this.connection = connection;
        this.statement = statement;

    }


//     {device_info.get("rule_name"),  //0  ruleName
//"2",   //1   executeRuleExecuteAction 1-rule,2-action
//"CDR", //2   featureName
//device_info.get("IMEI"),  //3   imei
//"0",  //4               sNofDevice
//device_info.get("file_name"), //5        fileName
//"0",           //6             DeviceType
//device_info.get("record_time"), //7      recordTime
//device_info.get("operator"),   //8       operator
//"error",         //9       DeviceIdType  imei/esn/meid  /error
//device_info.get("operator_tag"),  //10   operatorTag GSMA/CDMA
//device_info.get("period"),   //11        period
//device_info.get("MSISDN"),  //12         msisdn
//device_info.get("action"),    //13       action
//device_info.get("IMSI")  ,  //14         imsi   / txn_id
//device_info.get("record_type") ,   //15  recordType   /fileArray
//device_info.get("system_type"),    //16  systemType
//device_info.get("source") ,   //17       source
//device_info.get("raw_cdr_file_name") ,   //18 rawCdrFileName
//device_info.get("imei_arrival_time") ,   //19 imeiArrivalTime
//device_info.get("operator")  ,  //20           operator
//device_info.get("file_name")    //21         fileName
//
//                };
}
