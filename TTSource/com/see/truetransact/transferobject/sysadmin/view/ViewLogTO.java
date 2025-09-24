/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogTO.java
 * 
 * Created on Mon Apr 19 12:17:32 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.view;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOG.
 */
public class ViewLogTO extends TransferObject implements Serializable {

    private Date timeStamp = null;
    private String module = "";
    private String screen = "";
    private String ipAddr = "";
    private String branchId = "";
    private String userId = "";
    private String status = "";
    private String primaryKey = "";
    private String data = "";
    private String remarks = "";

    /**
     * Setter/Getter for TIME_STAMP - table Field
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Setter/Getter for MODULE - table Field
     */
    public void setModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    /**
     * Setter/Getter for SCREEN - table Field
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getScreen() {
        return screen;
    }

    /**
     * Setter/Getter for IP_ADDR - table Field
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * Setter/Getter for USER_ID - table Field
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for PRIMARY_KEY - table Field
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Setter/Getter for DATA - table Field
     */
    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("timeStamp", timeStamp));
        strB.append(getTOString("module", module));
        strB.append(getTOString("screen", screen));
        strB.append(getTOString("ipAddr", ipAddr));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("userId", userId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("primaryKey", primaryKey));
        strB.append(getTOString("data", data));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("timeStamp", timeStamp));
        strB.append(getTOXml("module", module));
        strB.append(getTOXml("screen", screen));
        strB.append(getTOXml("ipAddr", ipAddr));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("userId", userId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("primaryKey", primaryKey));
        strB.append(getTOXml("data", data));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}