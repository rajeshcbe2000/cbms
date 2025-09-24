/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * JobInspectionTO.java
 * 
 * Created on Thu Jun 10 15:43:46 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.audit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AUDIT_JOB_POSITION.
 */
public class JobInspectionTO extends TransferObject implements Serializable {

    private String auditId = "";
    private String jobCategory = "";
    private Double actualPosition = null;
    private String status = "";

    /**
     * Setter/Getter for AUDIT_ID - table Field
     */
    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getAuditId() {
        return auditId;
    }

    /**
     * Setter/Getter for JOB_CATEGORY - table Field
     */
    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    /**
     * Setter/Getter for ACTUAL_POSITION - table Field
     */
    public void setActualPosition(Double actualPosition) {
        this.actualPosition = actualPosition;
    }

    public Double getActualPosition() {
        return actualPosition;
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
        setKeyColumns(auditId + KEY_VAL_SEPARATOR + jobCategory);
        return auditId + KEY_VAL_SEPARATOR + jobCategory;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("auditId", auditId));
        strB.append(getTOString("jobCategory", jobCategory));
        strB.append(getTOString("actualPosition", actualPosition));
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
        strB.append(getTOXml("auditId", auditId));
        strB.append(getTOXml("jobCategory", jobCategory));
        strB.append(getTOXml("actualPosition", actualPosition));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}