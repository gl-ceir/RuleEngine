package com.gl.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {

    public boolean writeLogBlacklist(String logPath,String log) {
        boolean result = false;
        PrintWriter pw = null;
        File file = null;
        String fileName = null;

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
//        System.out.println(df.format(dateobj));

        try {
            fileName = "BlackListTacApiResults.log";
            file = new File(logPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(logPath + fileName);
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logPath + fileName, true)));
            if (file.length() == 0) {
                pw.println("Gsma Tac Logs ...");
            }
            pw.println(df.format(dateobj) + " : " + log);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    public boolean writeLogGsma(String logPath,String log) {
        boolean result = false;
        PrintWriter pw = null;
        File file = null;
        String fileName = null;
           DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
//        System.out.println(df.format(dateobj));
        try {
            fileName = "GsmaTacApiResults.log";
            file = new File(logPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(logPath + fileName);
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logPath + fileName, true)));
            if (file.length() == 0) {
                pw.println("Gsma Tac Logs ...");
            }
            pw.println(df.format(dateobj) + " : " + log);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

}
