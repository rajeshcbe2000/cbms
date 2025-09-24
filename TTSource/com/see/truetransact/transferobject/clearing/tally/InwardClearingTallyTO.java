/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingTallyTO.java
 * 
 * Created on Wed Jan 05 17:39:09 IST 2005
 */
package com.see.truetransact.transferobject.clearing.tally;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INWARD_TALLY.
 */
public class InwardClearingTallyTO extends TransferObject implements Serializable {

    private String tallyId = "";
    private String clearingType = "";
    private String scheduleNo = "";
    private Date clearingDt = null;
    private String status = "";
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String tallyStatus = "";

    /**
     * Setter/Getter for TALLY_ID - table Field
     */
    public void setTallyId(String tallyId) {
        this.tallyId = tallyId;
    }

    public String getTallyId() {
        return tallyId;
    }

    /**
     * Setter/Getter for CLEARING_TYPE - table Field
     */
    public void setClearingType(String clearingType) {
        this.clearingType = clearingType;
    }

    public String getClearingType() {
        return clearingType;
    }

    /**
     * Setter/Getter for SCHEDULE_NO - table Field
     */
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    /**
     * Setter/Getter for CLEARING_DT - table Field
     */
    public void setClearingDt(Date clearingDt) {
        this.clearingDt = clearingDt;
    }

    public Date getClearingDt() {
        return clearingDt;
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
     * Setter/Getter for TALLY_STATUS - table Field
     */
    public void setTallyStatus(String tallyStatus) {
        this.tallyStatus = tallyStatus;
    }

    public String getTallyStatus() {
        return tallyStatus;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("tallyId");
        return tallyId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tallyId", tallyId));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("clearingDt", clearingDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("tallyStatus", tallyStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tallyId", tallyId));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("clearingDt", clearingDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("tallyStatus", tallyStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}