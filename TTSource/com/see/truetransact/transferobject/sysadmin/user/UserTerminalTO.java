/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserTerminalTO.java
 *
 * Created on Thu Sep 09 15:43:09 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.user;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is USER_TERMINAL.
 */
public class UserTerminalTO extends TransferObject implements Serializable {

    private String userId = "";
    private String terminalId = "";
    private Date accessFromDt = null;
    private Date accessToDt = null;
    private String status = "";

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
     * Setter/Getter for TERMINAL_ID - table Field
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Setter/Getter for ACCESS_FROM_DT - table Field
     */
    public void setAccessFromDt(Date accessFromDt) {
        this.accessFromDt = accessFromDt;
    }

    public Date getAccessFromDt() {
        return accessFromDt;
    }

    /**
     * Setter/Getter for ACCESS_TO_DT - table Field
     */
    public void setAccessToDt(Date accessToDt) {
        this.accessToDt = accessToDt;
    }

    public Date getAccessToDt() {
        return accessToDt;
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
        setKeyColumns("userId" + KEY_VAL_SEPARATOR + "terminalId");
        return userId + KEY_VAL_SEPARATOR + terminalId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("userId", userId));
        strB.append(getTOString("terminalId", terminalId));
        strB.append(getTOString("accessFromDt", accessFromDt));
        strB.append(getTOString("accessToDt", accessToDt));
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
        strB.append(getTOXml("userId", userId));
        strB.append(getTOXml("terminalId", terminalId));
        strB.append(getTOXml("accessFromDt", accessFromDt));
        strB.append(getTOXml("accessToDt", accessToDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}