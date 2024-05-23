package com.gl.custom;

import com.gl.custom.model.CustomApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

import static com.gl.custom.dao.CustomQuery.*;
import static com.gl.custom.service.HttpApiConnection.getDataFromApi;

public class CustomCheck {
    static final Logger logger = LogManager.getLogger(CustomCheck.class);
    // create internally conn ->  or crete con on call
    public static boolean identifyCustomComplianceStatus(Connection conn, String imei, String source) {
        if (checkInGdceData(conn, imei)) {
            return true;
        } else {
            if (getSourceValueFromSysParam(conn, source)) {
                return checkFromApi(conn, imei);
            } else {
                return false;
            }
        }
    }

    private static boolean checkFromApi(Connection conn, String imei) {
        try {
            CustomApiResponse r = getDataFromApi(conn,imei);
            if (r.getResult().getCustoms_duty_tax().equalsIgnoreCase("paid")) {  // respose has imei GDCE tax paid
                saveInGdceData(conn, imei, r.getResult());
                saveInGdceApiCallHistory(conn, imei, r.getResult(), "true", "Success");
                return true;
            } else {
                saveInGdceApiCallHistory(conn, imei, r.getResult(), "false", "Success");
                return false;
            }
        } catch (Exception e) {
            saveInGdceApiCallHistory(conn, imei, "", "", "Success");
            return false;
        }

    }


}
