/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TerminalMasterTO.java
 * 
 * Created on Fri Apr 16 11:40:29 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.user;

import com.see.truetransact.transferobject.TransferObject;

import java.io.Serializable;

/**
 * Table name for this TO is TERMINAL_MASTER.
 */
public class TerminalMasterTO extends TransferObject implements Serializable {

    private String terminalName = "";
    private String ipAddr = "";
    private String machineName = "";
    private String branchCode = "";
    private String terminalDescription = "";
    private String terminalId = "";
    private String status = "";

    /**
     * Setter/Getter for TERMINAL_NAME - table Field
     */
    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalName() {
        return terminalName;
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
     * Setter/Getter for MACHINE_NAME - table Field
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineName() {
        return machineName;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for TERMINAL_DESCRIPTION - table Field
     */
    public void setTerminalDescription(String terminalDescription) {
        this.terminalDescription = terminalDescription;
    }

    public String getTerminalDescription() {
        return terminalDescription;
    }

    /**
     * Setter/Getter for TERMINAL_ID - table Field
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("terminalId");
        return "terminalId";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("terminalName", terminalName));
        strB.append(getTOString("ipAddr", ipAddr));
        strB.append(getTOString("machineName", machineName));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("terminalDescription", terminalDescription));
        strB.append(getTOString("terminalId", terminalId));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("terminalName", terminalName));
        strB.append(getTOXml("ipAddr", ipAddr));
        strB.append(getTOXml("machineName", machineName));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("terminalDescription", terminalDescription));
        strB.append(getTOXml("terminalId", terminalId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}