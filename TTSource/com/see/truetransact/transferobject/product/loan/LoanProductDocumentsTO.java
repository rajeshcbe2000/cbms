/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductDocumentsTO.java
 * 
 * Created on Thu Dec 23 11:47:07 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_DOC.
 */
public class LoanProductDocumentsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String docType = "";
    private String docNo = "";
    private String docDesc = "";
    private String status = "";
    private String sINo = "";

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
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
     * Setter/Getter for DOC_NO - table Field
     */
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getDocNo() {
        return docNo;
    }

    /**
     * Setter/Getter for DOC_DESC - table Field
     */
    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

    public String getDocDesc() {
        return docDesc;
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
     * Setter/Getter for SL_NO - table Field
     */
    public void setSINo(String sINo) {
        this.sINo = sINo;
    }

    public String getSINo() {
        return sINo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId" + KEY_VAL_SEPARATOR + "docType" + KEY_VAL_SEPARATOR + "docNo");
        return prodId + KEY_VAL_SEPARATOR + docType + KEY_VAL_SEPARATOR + docNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("docType", docType));
        strB.append(getTOString("docNo", docNo));
        strB.append(getTOString("docDesc", docDesc));
        strB.append(getTOString("status", status));
        strB.append(getTOString("sINo", sINo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("docType", docType));
        strB.append(getTOXml("docNo", docNo));
        strB.append(getTOXml("docDesc", docDesc));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("sINo", sINo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}