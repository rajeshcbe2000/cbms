/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroDocTO.java
 *
 * Created on Tue Apr 13 14:46:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_INTRO_DOC.
 */
public class IntroDocTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String docType = "";
    private Date issueDt = null;
    private Date expiryDt = null;
    private String issuedBy = "";
    private String docNo = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return depositNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("docType", docType));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("issuedBy", issuedBy));
        strB.append(getTOString("docNo", docNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("docType", docType));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("issuedBy", issuedBy));
        strB.append(getTOXml("docNo", docNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}