/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * WeeklyOffTO.java
 * 
 * Created on Tue May 17 15:32:18 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.calender;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is WEEKLY_OFF.
 */
public class WeeklyOffTO extends TransferObject implements Serializable {

    private String weeklyOff = "";
    private String weeklyOff1 = "";
    private String weeklyOff2 = "";
    private String halfDay1 = "";
    private String halfDay2 = "";
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";

    /**
     * Setter/Getter for WEEKLY_OFF - table Field
     */
    public void setWeeklyOff(String weeklyOff) {
        this.weeklyOff = weeklyOff;
    }

    public String getWeeklyOff() {
        return weeklyOff;
    }

    /**
     * Setter/Getter for WEEKLY_OFF1 - table Field
     */
    public void setWeeklyOff1(String weeklyOff1) {
        this.weeklyOff1 = weeklyOff1;
    }

    public String getWeeklyOff1() {
        return weeklyOff1;
    }

    /**
     * Setter/Getter for WEEKLY_OFF2 - table Field
     */
    public void setWeeklyOff2(String weeklyOff2) {
        this.weeklyOff2 = weeklyOff2;
    }

    public String getWeeklyOff2() {
        return weeklyOff2;
    }

    /**
     * Setter/Getter for HALF_DAY1 - table Field
     */
    public void setHalfDay1(String halfDay1) {
        this.halfDay1 = halfDay1;
    }

    public String getHalfDay1() {
        return halfDay1;
    }

    /**
     * Setter/Getter for HALF_DAY2 - table Field
     */
    public void setHalfDay2(String halfDay2) {
        this.halfDay2 = halfDay2;
    }

    public String getHalfDay2() {
        return halfDay2;
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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("weeklyOff", weeklyOff));
        strB.append(getTOString("weeklyOff1", weeklyOff1));
        strB.append(getTOString("weeklyOff2", weeklyOff2));
        strB.append(getTOString("halfDay1", halfDay1));
        strB.append(getTOString("halfDay2", halfDay2));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
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
        strB.append(getTOXml("weeklyOff", weeklyOff));
        strB.append(getTOXml("weeklyOff1", weeklyOff1));
        strB.append(getTOXml("weeklyOff2", weeklyOff2));
        strB.append(getTOXml("halfDay1", halfDay1));
        strB.append(getTOXml("halfDay2", halfDay2));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}