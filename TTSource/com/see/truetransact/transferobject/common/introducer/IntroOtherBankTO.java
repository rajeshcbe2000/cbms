/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroOtherBank.java
 * 
 * Created on Fri Dec 31 11:49:50 IST 2004
 */
package com.see.truetransact.transferobject.common.introducer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_INTRO_OTHERBANK.
 */
public class IntroOtherBankTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String bankName = "";
    private String branchName = "";
    private String otherActNum = "";
    private String otherActName = "";
    private String prodId = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for BANK_NAME - table Field
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    /**
     * Setter/Getter for BRANCH_NAME - table Field
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * Setter/Getter for OTHER_ACT_NUM - table Field
     */
    public void setOtherActNum(String otherActNum) {
        this.otherActNum = otherActNum;
    }

    public String getOtherActNum() {
        return otherActNum;
    }

    /**
     * Setter/Getter for OTHER_ACT_NAME - table Field
     */
    public void setOtherActName(String otherActName) {
        this.otherActName = otherActName;
    }

    public String getOtherActName() {
        return otherActName;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return "actNum";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("bankName", bankName));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("otherActNum", otherActNum));
        strB.append(getTOString("otherActName", otherActName));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("bankName", bankName));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("otherActNum", otherActNum));
        strB.append(getTOXml("otherActName", otherActName));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }
}