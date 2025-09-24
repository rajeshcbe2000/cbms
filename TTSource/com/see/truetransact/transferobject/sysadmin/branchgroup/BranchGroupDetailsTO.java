/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupDetailsTO.java
 * 
 * Created on Thu Aug 25 12:47:28 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.sysadmin.branchgroup;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BRANCH_GROUP_DETAILS.
 */
public class BranchGroupDetailsTO extends TransferObject implements Serializable {

    private String branchGroupId = "";
    private String branchId = "";
    private String status = "";

    /**
     * Setter/Getter for BRANCH_GROUP_ID - table Field
     */
    public void setBranchGroupId(String branchGroupId) {
        this.branchGroupId = branchGroupId;
    }

    public String getBranchGroupId() {
        return branchGroupId;
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
        setKeyColumns(branchGroupId + KEY_VAL_SEPARATOR + branchId);
        return branchGroupId + KEY_VAL_SEPARATOR + branchId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchGroupId", branchGroupId));
        strB.append(getTOString("branchId", branchId));
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
        strB.append(getTOXml("branchGroupId", branchGroupId));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}