/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.BlackList.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author user
 */
//@Entity
public class BlacklistTacDb implements Serializable {

    private int id;

    private String refcode, deviceid, responsestatus, partnerid, blockliststatus, generalliststatus, manufacturer, brandname, marketingname, modelname, band, operatingsys, nfc, bluetooth, WLAN, devicetype;

    List<BlacklistTacDeviceHistoryDb> imeihistory;

    public BlacklistTacDb() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRefcode() {
        return refcode;
    }

    public void setRefcode(String refcode) {
        this.refcode = refcode;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getResponsestatus() {
        return responsestatus;
    }

    public void setResponsestatus(String responsestatus) {
        this.responsestatus = responsestatus;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getBlockliststatus() {
        return blockliststatus;
    }

    public void setBlockliststatus(String blockliststatus) {
        this.blockliststatus = blockliststatus;
    }

    public String getGeneralliststatus() {
        return generalliststatus;
    }

    public void setGeneralliststatus(String generalliststatus) {
        this.generalliststatus = generalliststatus;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getMarketingname() {
        return marketingname;
    }

    public void setMarketingname(String marketingname) {
        this.marketingname = marketingname;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getOperatingsys() {
        return operatingsys;
    }

    public void setOperatingsys(String operatingsys) {
        this.operatingsys = operatingsys;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getWLAN() {
        return WLAN;
    }

    public void setWLAN(String WLAN) {
        this.WLAN = WLAN;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public List<BlacklistTacDeviceHistoryDb> getImeihistory() {
        return imeihistory;
    }

    public void setImeihistory(List<BlacklistTacDeviceHistoryDb> imeihistory) {
        this.imeihistory = imeihistory;
    }

   

    public BlacklistTacDb(int id, String refcode, String deviceid, String responsestatus, String partnerid, String blockliststatus, String generalliststatus, String manufacturer, String brandname, String marketingname, String modelname, String band, String operatingsys, String nfc, String bluetooth, String WLAN, String devicetype) {
        this.id = id;
        this.refcode = refcode;
        this.deviceid = deviceid;
        this.responsestatus = responsestatus;
        this.partnerid = partnerid;
        this.blockliststatus = blockliststatus;
        this.generalliststatus = generalliststatus;
        this.manufacturer = manufacturer;
        this.brandname = brandname;
        this.marketingname = marketingname;
        this.modelname = modelname;
        this.band = band;
        this.operatingsys = operatingsys;
        this.nfc = nfc;
        this.bluetooth = bluetooth;
        this.WLAN = WLAN;
        this.devicetype = devicetype;
    }

    @Override
    public String toString() {
        return "BlacklistTacDb{" + "id=" + id + ", refcode=" + refcode + ", deviceid=" + deviceid + ", responsestatus=" + responsestatus + ", partnerid=" + partnerid + ", blockliststatus=" + blockliststatus + ", generalliststatus=" + generalliststatus + ", manufacturer=" + manufacturer + ", brandname=" + brandname + ", marketingname=" + marketingname + ", modelname=" + modelname + ", band=" + band + ", operatingsys=" + operatingsys + ", nfc=" + nfc + ", bluetooth=" + bluetooth + ", WLAN=" + WLAN + ", devicetype=" + devicetype + ", imeihistory=" + imeihistory + '}';
    }

    
    
    
    
}
