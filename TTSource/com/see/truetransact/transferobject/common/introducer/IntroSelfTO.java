/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroSelf.java
 * 
 * Created on Fri Dec 31 11:44:35 IST 2004!!!!!obj : 
 */
package com.see.truetransact.transferobject.common.introducer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_INTRO_SELF.
 */
public class IntroSelfTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String actNumIntro = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String custId = "";
    private String acctNo = "";
    private String introacctNo = "";

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
     * Setter/Getter for ACT_NUM_INTRO - table Field
     */
    public void setActNumIntro(String actNumIntro) {
        this.actNumIntro = actNumIntro;
    }

    public String getActNumIntro() {
        return actNumIntro;
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
        strB.append(getTOString("actNumIntro", actNumIntro));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("introacctNo", introacctNo));
        strB.append(getTOXmlEnd());
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
        strB.append(getTOXml("actNumIntro", actNumIntro));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("introacctNo", introacctNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property acctNo.
     *
     * @return Value of property acctNo.
     */
    public java.lang.String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter for property acctNo.
     *
     * @param acctNo New value of property acctNo.
     */
    public void setAcctNo(java.lang.String acctNo) {
        this.acctNo = acctNo;
    }

    /**
     * Getter for property introacctNo.
     *
     * @return Value of property introacctNo.
     */
    public java.lang.String getIntroacctNo() {
        return introacctNo;
    }

    /**
     * Setter for property introacctNo.
     *
     * @param introacctNo New value of property introacctNo.
     */
    public void setIntroacctNo(java.lang.String introacctNo) {
        this.introacctNo = introacctNo;
    }
}