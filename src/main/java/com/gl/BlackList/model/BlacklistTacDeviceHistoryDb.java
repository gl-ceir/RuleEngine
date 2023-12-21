/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.BlackList.model;

import java.io.Serializable;

public class BlacklistTacDeviceHistoryDb implements Serializable {

    public int id;
    public String action, reasoncode, reasoncodedesc, by, country;
    public String date;

    public BlacklistTacDeviceHistoryDb(String action, String reasoncode, String reasoncodedesc, String by, String country, String date) {
        this.action = action;
        this.reasoncode = reasoncode;
        this.reasoncodedesc = reasoncodedesc;
        this.by = by;
        this.country = country;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReasoncode() {
        return reasoncode;
    }

    public void setReasoncode(String reasoncode) {
        this.reasoncode = reasoncode;
    }

    public String getReasoncodedesc() {
        return reasoncodedesc;
    }

    public void setReasoncodedesc(String reasoncodedesc) {
        this.reasoncodedesc = reasoncodedesc;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    

}
