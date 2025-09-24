/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchPhoneTO.java
 * 
 * Created on Mon Apr 11 16:05:02 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.branch;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BRANCH_PHONE.
 */
public class BranchPhoneTO extends TransferObject implements Serializable {

    private String branchCode = "";
    private String contactNo = "";
    private String contactType = "";
    private Integer phoneId;

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
     * Setter/Getter for CONTACT_NO - table Field
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    /**
     * Setter/Getter for CONTACT_TYPE - table Field
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactType() {
        return contactType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("branchCode");
        return branchCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("contactNo", contactNo));
        strB.append(getTOString("contactType", contactType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("contactNo", contactNo));
        strB.append(getTOXml("contactType", contactType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property phoneId.
     *
     * @return Value of property phoneId.
     */
    public Integer getPhoneId() {
        return phoneId;
    }

    /**
     * Setter for property phoneId.
     *
     * @param phoneId New value of property phoneId.
     */
    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }
}