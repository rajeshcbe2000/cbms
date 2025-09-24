/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchInspectionTO.java
 * 
 * Created on Wed Jun 09 15:34:25 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.audit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AUDIT_BRANCH.
 */
public class BranchInspectionTO extends TransferObject implements Serializable {

    private String auditId = "";
    private String classification = "";
    private String category = "";
    private String weeklyHoliday = "";
    private Double staffPosition = null;

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
     * Setter/Getter for CLASSIFICATION - table Field
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    /**
     * Setter/Getter for CATEGORY - table Field
     */
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Setter/Getter for WEEKLY_HOLIDAY - table Field
     */
    public void setWeeklyHoliday(String weeklyHoliday) {
        this.weeklyHoliday = weeklyHoliday;
    }

    public String getWeeklyHoliday() {
        return weeklyHoliday;
    }

    /**
     * Setter/Getter for STAFF_POSITION - table Field
     */
    public void setStaffPosition(Double staffPosition) {
        this.staffPosition = staffPosition;
    }

    public Double getStaffPosition() {
        return staffPosition;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(auditId);
        return auditId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("auditId", auditId));
        strB.append(getTOString("classification", classification));
        strB.append(getTOString("category", category));
        strB.append(getTOString("weeklyHoliday", weeklyHoliday));
        strB.append(getTOString("staffPosition", staffPosition));
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
        strB.append(getTOXml("classification", classification));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("weeklyHoliday", weeklyHoliday));
        strB.append(getTOXml("staffPosition", staffPosition));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}