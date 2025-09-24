/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroDocDetails.java
 * 
 * Created on Fri Dec 31 11:51:08 IST 2004
 */
package com.see.truetransact.transferobject.common.introducer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_INTRO_DOC.
 */
public class IntroDocDetailsTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String docType = "";
    private Date issueDt = null;
    private Date expiryDt = null;
    private String issuedBy = "";
    private String docNo = "";
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
     * Setter/Getter for DOC_TYPE - table Field
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocType() {
        return docType;
    }

    /**
     * Setter/Getter for ISSUE_DT - table Field
     */
    public void setIssueDt(Date issueDt) {
        this.issueDt = issueDt;
    }

    public Date getIssueDt() {
        return issueDt;
    }

    /**
     * Setter/Getter for EXPIRY_DT - table Field
     */
    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    /**
     * Setter/Getter for ISSUED_BY - table Field
     */
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    /**
     * Setter/Getter for DOC_NO - table Field
     */
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getDocNo() {
        return docNo;
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
        strB.append(getTOString("docType", docType));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("issuedBy", issuedBy));
        strB.append(getTOString("docNo", docNo));
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
        strB.append(getTOXml("docType", docType));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("issuedBy", issuedBy));
        strB.append(getTOXml("docNo", docNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}