/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanDocumentTO.java
 * 
 * Created on Thu Jun 09 12:38:36 IST 2005
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_DOC.
 */
public class AgriTermLoanDocumentTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String acctNo = "";
    private String docType = "";
    private String docFormNo = "";
    private String isSubmitted = "";
    private Date submittedDt = null;
    private String remarks = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date docExpDt = null;
    private String isMandatory = "";
    private String isExecuted = "";
    private Date docExecDt = null;

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
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
     * Setter/Getter for DOC_FORM_NO - table Field
     */
    public void setDocFormNo(String docFormNo) {
        this.docFormNo = docFormNo;
    }

    public String getDocFormNo() {
        return docFormNo;
    }

    /**
     * Setter/Getter for IS_SUBMITTED - table Field
     */
    public void setIsSubmitted(String isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public String getIsSubmitted() {
        return isSubmitted;
    }

    /**
     * Setter/Getter for SUBMITTED_DT - table Field
     */
    public void setSubmittedDt(Date submittedDt) {
        this.submittedDt = submittedDt;
    }

    public Date getSubmittedDt() {
        return submittedDt;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for DOC_EXP_DT - table Field
     */
    public void setDocExpDt(Date docExpDt) {
        this.docExpDt = docExpDt;
    }

    public Date getDocExpDt() {
        return docExpDt;
    }

    /**
     * Setter/Getter for IS_MANDATORY - table Field
     */
    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    /**
     * Setter/Getter for IS_EXECUTED - table Field
     */
    public void setIsExecuted(String isExecuted) {
        this.isExecuted = isExecuted;
    }

    public String getIsExecuted() {
        return isExecuted;
    }

    /**
     * Setter/Getter for DOC_EXEC_DT - table Field
     */
    public void setDocExecDt(Date docExecDt) {
        this.docExecDt = docExecDt;
    }

    public Date getDocExecDt() {
        return docExecDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNo");
        return acctNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("docType", docType));
        strB.append(getTOString("docFormNo", docFormNo));
        strB.append(getTOString("isSubmitted", isSubmitted));
        strB.append(getTOString("submittedDt", submittedDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("docExpDt", docExpDt));
        strB.append(getTOString("isMandatory", isMandatory));
        strB.append(getTOString("isExecuted", isExecuted));
        strB.append(getTOString("docExecDt", docExecDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("docType", docType));
        strB.append(getTOXml("docFormNo", docFormNo));
        strB.append(getTOXml("isSubmitted", isSubmitted));
        strB.append(getTOXml("submittedDt", submittedDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("docExpDt", docExpDt));
        strB.append(getTOXml("isMandatory", isMandatory));
        strB.append(getTOXml("isExecuted", isExecuted));
        strB.append(getTOXml("docExecDt", docExecDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}